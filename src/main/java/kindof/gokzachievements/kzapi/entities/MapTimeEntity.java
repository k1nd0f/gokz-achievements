package kindof.gokzachievements.kzapi.entities;

public class MapTimeEntity extends BaseEntity {
    private String map_id;
    private String stage;
    private String mode;
    private String tickrate;
    private String time;
    private String teleports;
    private String updated_by;
    private String record_filter_id;
    private String server_name;
    private String map_name;
    private String points;
    private String replay_id;

    public MapTimeEntity() {

    }

    public void setMap_id(String map_id) {
        this.map_id = map_id;
    }

    public void setStage(String stage) {
        this.stage = stage;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public void setTickrate(String tickrate) {
        this.tickrate = tickrate;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setTeleports(String teleports) {
        this.teleports = teleports;
    }

    public void setUpdated_by(String updated_by) {
        this.updated_by = updated_by;
    }

    public void setRecord_filter_id(String record_filter_id) {
        this.record_filter_id = record_filter_id;
    }

    public void setServer_name(String server_name) {
        this.server_name = server_name;
    }

    public void setMap_name(String map_name) {
        this.map_name = map_name;
    }

    public void setPoints(String points) {
        this.points = points;
    }

    public void setReplay_id(String replay_id) {
        this.replay_id = replay_id;
    }

    public String getMap_id() {
        return map_id;
    }

    public String getStage() {
        return stage;
    }

    public String getMode() {
        return mode;
    }

    public String getTickrate() {
        return tickrate;
    }

    public String getTime() {
        return time;
    }

    public String getTeleports() {
        return teleports;
    }

    public String getUpdated_by() {
        return updated_by;
    }

    public String getRecord_filter_id() {
        return record_filter_id;
    }

    public String getServer_name() {
        return server_name;
    }

    public String getMap_name() {
        return map_name;
    }

    public String getPoints() {
        return points;
    }

    public String getReplay_id() {
        return replay_id;
    }
}
