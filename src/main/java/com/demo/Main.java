package com.demo;

import lombok.SneakyThrows;

import javax.sql.DataSource;

public class Main {
    @SneakyThrows
    public static void main(String[] args) {
        var dataSource = initializePooledDataSource();
        var total = 0.0;
        var start = System.nanoTime();
        for (int i = 0; i < 500; i++) {
            try (var connection = dataSource.getConnection()) {
                connection.setAutoCommit(false);
                try (var statement = connection.createStatement()) {
                    var rs = statement.executeQuery("select random() from products");
                    rs.next();
                    total += rs.getDouble(1);
                }
                connection.rollback();
            }
        }
        System.out.println((System.nanoTime() - start) / 1000_000 + " ms");
        System.out.println(total);
    }

    public static DataSource initializePooledDataSource() {
        int threadCount = 10;
        PooledDataSource pooledDataSource = new PooledDataSource(threadCount);
        pooledDataSource.setURL("jdbc:postgresql://localhost:5432/postgres");
        pooledDataSource.setUser("ju22user");
        pooledDataSource.setPassword("ju22pass");
        return pooledDataSource;
    }
}
