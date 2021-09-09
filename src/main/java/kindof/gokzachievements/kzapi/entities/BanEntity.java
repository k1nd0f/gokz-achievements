package kindof.gokzachievements.kzapi.entities;

public class BanEntity extends BaseEntity {
    private String ban_type;
    private String expires_on;
    private String notes;
    private String stats;
    private String updated_by_id;

    public BanEntity() {

    }

    public void setBan_type(String ban_type) {
        this.ban_type = ban_type;
    }

    public void setExpires_on(String expires_on) {
        this.expires_on = expires_on;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public void setStats(String stats) {
        this.stats = stats;
    }

    public void setUpdated_by_id(String updated_by_id) {
        this.updated_by_id = updated_by_id;
    }

    public String getBan_type() {
        return ban_type;
    }

    public String getExpires_on() {
        return expires_on;
    }

    public String getNotes() {
        return notes;
    }

    public String getStats() {
        return stats;
    }

    public String getUpdated_by_id() {
        return updated_by_id;
    }
}
