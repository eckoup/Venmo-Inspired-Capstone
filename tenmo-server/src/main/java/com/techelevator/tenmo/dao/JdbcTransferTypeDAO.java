package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.TransferType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import javax.sql.DataSource;

public class JdbcTransferTypeDAO implements TransferTypeDAO {

    private JdbcTemplate jdbcTemplate;

    public JdbcTransferTypeDAO(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public TransferType getTransferTypeFromDescription(String description) {
        String sql = "SELECT transfer_type_id, transfer_type_desc FROM transfer_types WHERE transfer_type_desc = ?";

        SqlRowSet result = jdbcTemplate.queryForRowSet(sql, description);
        TransferType transferType = null;

        if(result.next()) {
            int transferTypeId = result.getInt("transfer_type_id");
            String transferTypeDescription = result.getString("transfer_type_desc");

            transferType = new TransferType(transferTypeId, transferTypeDescription);
        }
        return transferType;
    }

    @Override
    public TransferType getTransferTypeFromId(int transferId) {
        String sql = "SELECT transfer_type_id, transfer_type_desc FROM transfer_types WHERE transfer_type_id = ?";
        SqlRowSet result = jdbcTemplate.queryForRowSet(sql, transferId);

        TransferType transferType = null;
        if(result.next()) {

            int transferTypeId = result.getInt("transfer_type_id");
            String transferTypeDesc = result.getString("transfer_type_desc");

            transferType = new TransferType(transferTypeId, transferTypeDesc);
        }


        return transferType;
    }

}
