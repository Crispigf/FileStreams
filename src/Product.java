import java.nio.ByteBuffer;

public class Product {
    private static final int NAME_LENGTH = 35;
    private static final int DESCRIPTION_LENGTH = 75;
    private static final int ID_LENGTH = 6;
    private static final int RECORD_SIZE = NAME_LENGTH + DESCRIPTION_LENGTH + ID_LENGTH + 8; // 8 for double

    private String ID;
    private String Name;
    private String Description;
    private Double Cost = 0.0;

    public Product() {
    }

    public Product(String ID, String name, String description, Double cost) {
        this.ID = ID;
        Name = name;
        Description = description;
        Cost = cost;
    }

    public String getID() {
        return ID;
    }

    public String getName() {
        return Name;
    }

    public String getDescription() {
        return Description;
    }

    public Double getCost() {
        return Cost;
    }

    public void setName(String name) {
        Name = name;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public void setCost(Double cost) {
        Cost = cost;
    }

    public String toFormattedString() {
        return String.format(Name + "," + Description + "," + ID+ "," + Cost + "\n");
    }

    public byte[] toBytes() {
        ByteBuffer buffer = ByteBuffer.allocate(RECORD_SIZE);
        buffer.put(padField(Name, NAME_LENGTH).getBytes());
        buffer.put(padField(Description, DESCRIPTION_LENGTH).getBytes());
        buffer.put(padField(ID, ID_LENGTH).getBytes());
        buffer.putDouble(Cost);
        return buffer.array();
    }

    public static int getRecordSize() {
        return RECORD_SIZE;
    }

    private String padField(String data, int length) {
        if (data.length() > length) {
            return data.substring(0, length);
        } else {
            StringBuilder paddedData = new StringBuilder(data);
            for (int i = data.length(); i < length; i++) {
                paddedData.append(" ");
            }
            return paddedData.toString();
        }
    }
}

