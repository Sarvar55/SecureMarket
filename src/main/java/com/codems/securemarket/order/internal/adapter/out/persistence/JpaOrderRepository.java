package com.codems.securemarket.order.internal.adapter.out.persistence;

import java.util.List;
import java.util.Optional;
import java.time.Instant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

interface JpaOrderRepository extends JpaRepository<OrderEntity, Long> {
    Optional<OrderEntity> findByIdAndCustomerId(Long id, Long customerId);
    List<OrderEntity> findAllByCustomerIdOrderByCreatedAtDesc(Long customerId);

    @Query(value = """
            SELECT
                COALESCE(SUM(total_amount) FILTER (
                    WHERE created_at >= :currentStart
                      AND status IN ('PAID', 'PROCESSING', 'SHIPPED', 'DELIVERED')
                ), 0) AS "currentRevenue",
                COUNT(*) FILTER (
                    WHERE created_at >= :currentStart
                      AND status IN ('PAID', 'PROCESSING', 'SHIPPED', 'DELIVERED')
                ) AS "currentOrderCount",
                COUNT(*) FILTER (
                    WHERE created_at >= :currentStart AND status = 'PENDING_PAYMENT'
                ) AS "pendingOrderCount",
                COUNT(*) FILTER (
                    WHERE created_at >= :currentStart AND status = 'PAYMENT_FAILED'
                ) AS "failedOrderCount",
                COUNT(*) FILTER (
                    WHERE created_at >= :currentStart AND status = 'DELIVERED'
                ) AS "deliveredOrderCount",
                COALESCE(SUM(total_amount) FILTER (
                    WHERE created_at >= :previousStart
                      AND created_at < :currentStart
                      AND status IN ('PAID', 'PROCESSING', 'SHIPPED', 'DELIVERED')
                ), 0) AS "previousRevenue",
                COUNT(*) FILTER (
                    WHERE created_at >= :previousStart
                      AND created_at < :currentStart
                      AND status IN ('PAID', 'PROCESSING', 'SHIPPED', 'DELIVERED')
                ) AS "previousOrderCount"
            FROM orders
            WHERE created_at >= :previousStart
              AND created_at < :currentEnd
              AND currency = :currency
            """, nativeQuery = true)
    OrderAnalyticsSummaryProjection summarize(
            @Param("currentStart") Instant currentStart,
            @Param("currentEnd") Instant currentEnd,
            @Param("previousStart") Instant previousStart,
            @Param("currency") String currency
    );

    @Query(value = """
            SELECT
                CAST(created_at AT TIME ZONE 'UTC' AS DATE) AS "saleDate",
                SUM(total_amount) AS revenue,
                COUNT(*) AS "orderCount"
            FROM orders
            WHERE created_at >= :currentStart
              AND created_at < :currentEnd
              AND currency = :currency
              AND status IN ('PAID', 'PROCESSING', 'SHIPPED', 'DELIVERED')
            GROUP BY CAST(created_at AT TIME ZONE 'UTC' AS DATE)
            ORDER BY saleDate
            """, nativeQuery = true)
    List<SalesTrendProjection> findSalesTrend(
            @Param("currentStart") Instant currentStart,
            @Param("currentEnd") Instant currentEnd,
            @Param("currency") String currency
    );

    @Query(value = """
            SELECT
                item.product_id AS "productId",
                (ARRAY_AGG(item.product_name ORDER BY purchase_order.created_at DESC))[1]
                    AS "productName",
                SUM(item.quantity) AS "unitsSold",
                SUM(item.unit_price * item.quantity) AS revenue
            FROM order_items item
            JOIN orders purchase_order ON purchase_order.id = item.order_id
            WHERE purchase_order.created_at >= :currentStart
              AND purchase_order.created_at < :currentEnd
              AND purchase_order.currency = :currency
              AND purchase_order.status IN ('PAID', 'PROCESSING', 'SHIPPED', 'DELIVERED')
            GROUP BY item.product_id
            ORDER BY unitsSold DESC, revenue DESC
            LIMIT :limit
            """, nativeQuery = true)
    List<TopProductProjection> findTopProducts(
            @Param("currentStart") Instant currentStart,
            @Param("currentEnd") Instant currentEnd,
            @Param("currency") String currency,
            @Param("limit") int limit
    );
}
