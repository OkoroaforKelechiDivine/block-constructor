import java.util.HashSet;
import java.util.Set;

class MempoolTransaction {
    String txid;
    int fee;
    int weight;
    Set<String> parents;

    public MempoolTransaction(String txid, int fee, int weight, Set<String> parents) {
        this.txid = txid;
        this.fee = fee;
        this.weight = weight;
        this.parents = parents;
    }

    // Add a static method to create a transaction without parents
    public static MempoolTransaction createTransactionWithoutParents(String txid, int fee, int weight) {
        return new MempoolTransaction(txid, fee, weight, new HashSet<>());
    }
}
