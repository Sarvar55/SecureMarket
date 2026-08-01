package com.codems.securemarket.analytics.internal.application.port.in.query;

public record DashboardQuery(
                int days,
                String currency,
                int topProductLimit) {
        private void validate() {
                if (days <= 0) {
                        throw new IllegalArgumentException("days must be positive");
                }
                if (currency == null || !currency.matches("(?i)[A-Z]{3}")) {
                        throw new IllegalArgumentException("currency must contain three letters");
                }
                if (topProductLimit < 1 || topProductLimit > 20) {
                        throw new IllegalArgumentException("topProductLimit must be between 1 and 20");
                }
        }

        public static DashboardQuery of(int days, String currency, int topProductLimit) {
                var dashboardQuery = new DashboardQuery(days, currency, topProductLimit);
                dashboardQuery.validate();
                return dashboardQuery;
        }

}
