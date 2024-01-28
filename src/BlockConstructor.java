import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class BlockConstructor {
    public static List<MempoolTransaction> parseMempoolCSV(String filePath) throws IOException {
        List<MempoolTransaction> transactions = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                String txid = parts[0];
                int fee = Integer.parseInt(parts[1]);
                int weight = Integer.parseInt(parts[2]);

                // Handle the case where parent_txids field might be missing or empty
                Set<String> parents = parts.length > 3 && !parts[3].isEmpty() ? new HashSet<>(Arrays.asList(parts[3].split(";"))) : new HashSet<>();

                transactions.add(new MempoolTransaction(txid, fee, weight, parents));
            }
        }

        return transactions;
    }
    public static Set<String> getMaxFeeBlock(List<MempoolTransaction> transactions) {
        transactions.sort(Comparator.comparingInt((MempoolTransaction t) -> t.fee).reversed());
        int blockWeight = 0;
        Set<String> selectedTxids = new HashSet<>();

        for (MempoolTransaction transaction : transactions) {
            if (!selectedTxids.contains(transaction.txid)) {
                if (transaction.parents.isEmpty() || selectedTxids.containsAll(transaction.parents)) {
                    if (blockWeight + transaction.weight <= 4000000) {
                        selectedTxids.add(transaction.txid);
                        blockWeight += transaction.weight;
                    }
                }
            }
        }

        return selectedTxids;
    }

    public static void main(String[] args) {
        String mempoolFilePath = "src/mempool.csv";

        try {
            List<MempoolTransaction> mempoolTransactions = parseMempoolCSV(mempoolFilePath);
            Set<String> selectedTxids = getMaxFeeBlock(mempoolTransactions);

            for (String txid : selectedTxids) {
                System.out.println(txid);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}