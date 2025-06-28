package preInit

import gregtech.api.pipenet.tile.IPipeTile
import groovy.transform.TupleConstructor
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet
import net.minecraft.util.EnumFacing
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

class PipeOperationWalker<T extends IPipeTile> {

    // Copied from normal pipe net walker class
    // I hate everything being private in ceu...
    PipeOperationWalker root
    World world
    Set<T> walked
    List<EnumFacing> nextPipeFacings = []
    List<T> nextPipes = []
    List<PipeOperationWalker> walkers
    BlockPos.MutableBlockPos currentPos
    T currentPipe
    EnumFacing from = null
    int walkedBlocks
    boolean invalid
    boolean running
    boolean failed = false

    Class<T> basePipeClass

    // Function to run on every pipe
    // If false is returned then halt the walker
    ITraverseOption option
    boolean reverse = false

    // Only exists for the root walker
    EnumFacing direction

    static int collectPipeNet(World world, BlockPos sourcePipe, IPipeTile pipe,
                              EnumFacing direction, ITraverseOption option, int maxWalks) {
        new PipeOperationWalker(world, sourcePipe, 0, pipe.class).with {
            setCurrentPipe pipe
            setDirection direction
            setOption option
            traverse maxWalks
            getWalkedBlocks()
        }
    }

    void traverse(int maxWalks) {
        if (invalid) {
            throw new IllegalStateException('This walker already walked!')
        }
        root = this
        walked = new ObjectOpenHashSet<>()
        int i = 0
        running = true
        while (running && !step() && i++ < maxWalks) {
            /* Do nothing */
        }
        walkedBlocks = i
        running = false
        walked = null
        if (walkedBlocks >= maxWalks) {
            log.fatalMC("The walker reached the maximum amount of walks ${walkedBlocks}")
        }
        invalid = true
    }

    boolean step() {
        if (walkers == null) {
            if (!checkCurrent()) {
                root.failed = true
                return true
            }

            if (nextPipeFacings.empty) return true
            if (nextPipeFacings.size() == 1) {

                def next = nextPipes[0]
                def into = nextPipeFacings[0]

                root.option.operate(into, currentPipe, next, reverse)

                currentPos.setPos(next.pipePos)
                currentPipe = next
                from = into.opposite
                walkedBlocks++

                return !root.running
            }

            walkers = []
            for (int i = 0; i < nextPipeFacings.size(); i++) {
                def into = nextPipeFacings[i]

                def walker = Objects.requireNonNull(
                        createSubWalker(world, into, currentPos.offset(into), walkedBlocks + 1),
                        'Walker can\'t be null')

                def nextPipe = nextPipes[i]

                root.option.operate(into, currentPipe, nextPipe, walker.reverse)

                walker.root = this.root
                walker.currentPipe = nextPipe
                walker.from = into.opposite
                walkers.add(walker)
            }
        }

        walkers.removeIf { it.step() }

        return !root.running || walkers.empty
    }

    boolean checkCurrent() {
        nextPipeFacings.clear()
        nextPipes.clear()
        if (currentPipe == null) {
            def thisPipe = world.getTileEntity(currentPos)
            if (!(thisPipe instanceof IPipeTile)) {
                log.fatalMC("PipeWalker expected a pipe, but found ${thisPipe} at ${currentPos}")
                return false
            }
            if (!basePipeClass.isAssignableFrom(thisPipe.class)) {
                return false
            }
            currentPipe = (T) thisPipe
        }
        T pipeTile = currentPipe

        this.root.walked.add(pipeTile)

        def facings = [] as List<EnumFacing>
        def next = root.option.findNext(from ?: direction, pipeTile)

        if (next) {
            facings.add(next)
        }

        if (walkedBlocks == 0) {
            facings.add(direction) // Special case for the root node
        }

        for (side in facings) {
            def tile = pipeTile.getNeighbor(side)
            if (tile && basePipeClass.isAssignableFrom(tile.class)) {
                T otherPipe = (T) tile
                if (!isWalked(otherPipe)) {
                    nextPipeFacings.add(side)
                    nextPipes.add(otherPipe)
                }
            }
        }
        return true
    }

    private PipeOperationWalker(World world, BlockPos sourcePipe, int walkedBlocks, Class<T> basePipeClass) {
        this.with {
            setWorld Objects.requireNonNull(world)
            setWalkedBlocks walkedBlocks
            setCurrentPos new BlockPos.MutableBlockPos(Objects.requireNonNull(sourcePipe))
            setRoot this
            setBasePipeClass basePipeClass
        }
    }

    PipeOperationWalker createSubWalker(World world, EnumFacing facingToNextPos, BlockPos nextPos, int walkedBlocks) {
        def reverse = direction ? facingToNextPos != direction : this.reverse
        new PipeOperationWalker(world, nextPos, walkedBlocks, this.basePipeClass).tap {
            setReverse reverse
        }
    }

    boolean isWalked(T pipe) {
        root.walked.contains(pipe)
    }

    // Returns the other facing this pipe has a neighbouring pipe to connect to, for CONNECTING operation type
    // Returns null if:
    //   1) The pipe is cannot find any pipe to connect
    //   2) The pipe is connected to more than 1 side alr
    static final Closure<EnumFacing> FIND_TO_CONNECT = { EnumFacing from, IPipeTile pipe ->
        EnumFacing ret = null
        int count = 0

        for (facing in EnumFacing.values()) {
            if (facing == from) continue
            def other = pipe.getNeighbor(facing)
            if (other instanceof IPipeTile && pipe.class.isAssignableFrom(other.class)) {
                if (!ret && (other as IPipeTile).connections == 0) {
                    ret = facing
                }
                count++
            }
        }

        // Note that ret can still be null even if count == 0
        return count == 1 ? ret : null
    }

    // Returns the other facing this pipe is connected to, for DISCONNECTING operation type
    // Returns null if:
    //   1) The pipe is not connected to any other side
    //   2) The pipe is connected to more than 1 side among other sides
    static final Closure<EnumFacing> FIND_CONNECTED = { EnumFacing from, IPipeTile pipe ->
        int former = from.index,
            next = -1,
            connections = pipe.connections,
            count = 0


        for (current in 0..5) {
            if (current == former) continue
            if ((connections & 1 << current) > 0) {
                count++
                if (count > 1) {
                    return null
                } else {
                    next = current
                }
            }
        }
        return next != -1 ? EnumFacing.byIndex(next) : null
    }

    // Connects this pipe with the other
    static final Closure<Void> CONNECTOR = { EnumFacing facingToOther, IPipeTile self, IPipeTile other, boolean reverse ->
        self.setConnection(facingToOther, true, false)
    }

    // Disconnects this pipe with the other
    static final Closure<Void> DISCONNECTOR = { EnumFacing facingToOther, IPipeTile self, IPipeTile other, boolean reverse ->
        self.setConnection(facingToOther, false, false)
    }

    // Blocks this pipe with the other
    static final Closure<Void> BLOCKER = { EnumFacing facingToOther, IPipeTile self, IPipeTile other, boolean reverse ->
        reverse ? other.setFaceBlocked(facingToOther.opposite, true) : self.setFaceBlocked(facingToOther, true)
    }

    // Blocks this pipe with the other
    static final Closure<Void> UNBLOCKER = { EnumFacing facingToOther, IPipeTile self, IPipeTile other, boolean reverse ->
        reverse ? other.setFaceBlocked(facingToOther.opposite, false) : self.setFaceBlocked(facingToOther, false)
    }

    @TupleConstructor
    static enum TraverseOption implements ITraverseOption {

        CONNECTING(FIND_TO_CONNECT, CONNECTOR),
        DISCONNECTING(FIND_CONNECTED, DISCONNECTOR),
        BLOCKING(FIND_CONNECTED, BLOCKER),
        UNBLOCKING(FIND_CONNECTED, UNBLOCKER);
        // TODO: Painter

        final Closure<EnumFacing> pathFinder
        final Closure<Void> pipeOperator

        @Override
        EnumFacing findNext(EnumFacing from, IPipeTile pipe) {
            pathFinder(from, pipe)
        }

        @Override
        void operate(EnumFacing from, IPipeTile self, IPipeTile other, boolean reverse) {
            pipeOperator(from, self, other, reverse)
        }
    }

    interface ITraverseOption {

        EnumFacing findNext(EnumFacing from, IPipeTile pipe)

        void operate(EnumFacing from, IPipeTile self, IPipeTile other, boolean reverse)
    }
}