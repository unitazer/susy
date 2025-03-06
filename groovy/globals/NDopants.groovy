package globals

class NDopants {

    static class NDopant {
        String metaItemName
        int efficiency

        NDopant(String metaItemName, int efficiency) {
            this.metaItemName = metaItemName
            this.efficiency = efficiency
        }
    }

    public static final ndopants = [
        new NDopant("dustHighPurityBoron", 1),
        new NDopant("dustHighPurityGallium", 2),
        new NDopant("dustHighPurityAluminium", 2),
        new NDopant("dustTinyHighPurityIndium", 1)
    ]
}

