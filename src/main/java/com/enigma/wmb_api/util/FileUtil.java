package com.enigma.wmb_api.util;

import com.enigma.wmb_api.constant.ResponseMessage;
import com.enigma.wmb_api.entity.Transaction;
import com.enigma.wmb_api.model.DailyReportModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@Component
public class FileUtil {
    public final Path directoryPath;

    private final String CSV_HEADER_DAILY = "date\ttrx_id\tcustomer_id\tdesc\ttable_name\ttotal\tstatus";
    private final String CSV_HEADER_MONTHLY = "date\ttotal";

    public FileUtil(
            @Value("${wmb_api.multipart.path-location}") Path directoryPath
    ) {
        this.directoryPath = directoryPath;
    }

    public UrlResource generateCsv(List<DailyReportModel> transactions, String filename, boolean isSummarized) {
        StringBuilder csv = new StringBuilder();
        csv.append(isSummarized ? CSV_HEADER_MONTHLY : CSV_HEADER_DAILY);
        csv.append("\n");
        for (DailyReportModel transaction : transactions) {
            csv.append(DateUtil.parseDate(transaction.getTrxDate()));
            csv.append("\t");
            csv.append(transaction.getTrxId());
            csv.append("\t");
            csv.append(transaction.getUserId());
            csv.append("\t");
            csv.append(transaction.getDescription());
            csv.append("\t");
            csv.append(transaction.getTableName());
            csv.append("\t");
            csv.append(transaction.getTotal());
            csv.append("\t");
            csv.append(transaction.getStatus());
            csv.append("\n");
        }

        try {
            Path path = directoryPath.resolve(filename);
            Files.writeString(path, csv.toString());
            if (Files.notExists(path)) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, ResponseMessage.ERROR_NOT_FOUND);
            }
            return new UrlResource(path.toUri());
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    public Resource generatePdf(List<Transaction> transactions){
        return null;
    }
}
