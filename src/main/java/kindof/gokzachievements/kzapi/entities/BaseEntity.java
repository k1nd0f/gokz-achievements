package kindof.gokzachievements.kzapi.entities;

public class BaseEntity {
    private String id;
    private String player_name;
    private String steam_id;
    private String steamid64;
    private String server_id;
    private String created_on;
    private String updated_on;

    public BaseEntity() {

    }

    public void setId(String id) {
        this.id = id;
    }

    public void setPlayer_name(String player_name) {
        this.player_name = player_name;
    }

    public void setSteam_id(String steam_id) {
        this.steam_id = steam_id;
    }

    public void setSteamid64(String steamid64) {
        this.steamid64 = steamid64;
    }

    public void setServer_id(String server_id) {
        this.server_id = server_id;
    }

    public void setCreated_on(String created_on) {
        this.created_on = created_on;
    }

    public void setUpdated_on(String updated_on) {
        this.updated_on = updated_on;
    }

    public String getId() {
        return id;
    }

    public String getPlayer_name() {
        return player_name;
    }

    public String getSteam_id() {
        return steam_id;
    }

    public String getSteamid64() {
        return steamid64;
    }

    public String getServer_id() {
        return server_id;
    }

    public String getCreated_on() {
        return created_on;
    }

    public String getUpdated_on() {
        return updated_on;
    }
}
