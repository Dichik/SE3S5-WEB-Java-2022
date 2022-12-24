package org.agency.repository.manager;

import org.agency.entity.Manager;
import org.agency.exception.EntityNotFoundException;
import org.agency.repository.DaoImpl;
import org.agency.repository.PersonRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.Optional;

@Repository
public class ManagerRepository extends DaoImpl<Manager> implements PersonRepository<Manager> {
    private static final Logger logger = LogManager.getLogger(ManagerRepository.class);

    @Autowired
    public ManagerRepository(Connection connection) {
        super(connection, "managers");

        this.createTable();
    }

    @Override
    public String getTableSQLSchema() {
        return "CREATE TABLE IF NOT EXISTS managers(" +
                "id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY, " +
                "email VARCHAR(255), " +
                "password VARCHAR(255))";
    }

    @Override
    public String getInsertSQLQuery() {
        return "INSERT INTO " + this.tableName +
                " (email, password) " +
                "VALUES (?, ?)";
    }

    @Override
    public Manager buildItem(ResultSet rs) throws SQLException {
        return new Manager.ManagerBuilder(rs.getString("email"),
                rs.getString("password")).build();
    }

    @Override
    public void updatePreparedStatementWithItemData(PreparedStatement ps, Manager manager, boolean setId) throws SQLException {
        if (manager.getEmail() != null) {
            ps.setString(1, manager.getEmail());
        } else ps.setNull(1, Types.NULL);

        if (manager.getPassword() != null) {
            ps.setString(2, manager.getPassword());
        } else ps.setNull(2, Types.NULL);

        if (setId) {
            ps.setLong(3, manager.getId());
        }

    }

    @Override
    public String getUpdateSQLQuery() {
        return "UPDATE " + this.tableName +
                " SET email=?, password=?" +
                " WHERE id=?";
    }

    @Override
    public Optional<Manager> findByEmail(String email) throws EntityNotFoundException {
        try {
            Statement statement = this.connection.createStatement();
            String sql = "SELECT * FROM " + this.tableName + " WHERE email='" + email + "'";
            ResultSet rs = statement.executeQuery(sql);
            Manager item = null;
            while (rs.next()) {
                item = this.buildItem(rs);
            }
            return Optional.ofNullable(item);
        } catch (SQLException e) {
            String message = String.format("Couldn't get item with email=%s, see: %s", email, e);
            logger.error(message);
            throw new EntityNotFoundException(message);
        }
    }

    @Override
    public boolean existsByEmail(String email) {
        try {
            this.findByEmail(email);
            return true;
        } catch (EntityNotFoundException e) {
            return false;
        }
    }

}
