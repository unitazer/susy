package globals

class PDopants {

    static class PDopant {
        String metaItemName
        int efficiency

        PDopant(String metaItemName, int efficiency) {
            this.metaItemName = metaItemName
            this.efficiency = efficiency
        }
    }

    public static final pdopants = [
        new PDopant("dustHighPurityAntimony", 1),
        new PDopant("dustHighPurityPhosphorus", 2),
        new PDopant("dustHighPurityArsenic", 2)
    ]
}
