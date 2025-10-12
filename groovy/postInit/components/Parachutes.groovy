import globals.Globals;

ASSEMBLER = recipemap('assembler')

ASSEMBLER.recipeBuilder()
        .circuitMeta(2)
        .inputs(metaitem('foilPolycaprolactam') * 64)
        .inputs(metaitem('threadKevlar') * 32)
        .outputs(metaitem('parachute.drogue'))
        .duration(200)
        .EUt(480)
        .buildAndRegister();

ASSEMBLER.recipeBuilder()
        .circuitMeta(3)
        .inputs(metaitem('foilPolycaprolactam') * 64)
        .inputs(metaitem('foilPolycaprolactam') * 64)
        .inputs(metaitem('foilPolycaprolactam') * 64)
        .inputs(metaitem('foilPolycaprolactam') * 64)
        .inputs(metaitem('foilPolycaprolactam') * 64)
        .inputs(metaitem('threadKevlar') * 64)
        .inputs(metaitem('threadKevlar') * 32)
        .outputs(metaitem('parachute.main'))
        .duration(600)
        .EUt(480)
        .buildAndRegister();

ASSEMBLER.recipeBuilder()
        .circuitMeta(4)
        .inputs(metaitem('foilPolycaprolactam') * 64)
        .inputs(metaitem('foilPolycaprolactam') * 64)
        .inputs(metaitem('foilPolycaprolactam') * 64)
        .inputs(metaitem('foilPolycaprolactam') * 64)
        .inputs(metaitem('foilPolyethyleneTerephthalate') * 64)
        .inputs(metaitem('foilPolyethyleneTerephthalate') * 64)
        .inputs(metaitem('threadKevlar') * 64)
        .inputs(metaitem('threadKevlar') * 32)
        .outputs(metaitem('parachute.supersonic_main'))
        .duration(600)
        .EUt(480)
        .buildAndRegister();