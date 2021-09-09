package kindof.gokzachievements.kzapi.entities;

public class JumpEntity extends BaseEntity {
    private String jump_type;
    private String distance;
    private String tickrate;
    private String msl_count;
    private String strafe_count;
    private String is_crouch_bind;
    private String is_forward_bind;
    private String is_crouch_boost;
    private String updated_by_id;

    public JumpEntity() {

    }

    public void setJump_type(String jump_type) {
        this.jump_type = jump_type;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public void setTickrate(String tickrate) {
        this.tickrate = tickrate;
    }

    public void setMsl_count(String msl_count) {
        this.msl_count = msl_count;
    }

    public void setStrafe_count(String strafe_count) {
        this.strafe_count = strafe_count;
    }

    public void setIs_crouch_bind(String is_crouch_bind) {
        this.is_crouch_bind = is_crouch_bind;
    }

    public void setIs_forward_bind(String is_forward_bind) {
        this.is_forward_bind = is_forward_bind;
    }

    public void setIs_crouch_boost(String is_crouch_boost) {
        this.is_crouch_boost = is_crouch_boost;
    }

    public void setUpdated_by_id(String updated_by_id) {
        this.updated_by_id = updated_by_id;
    }

    public String getJump_type() {
        return jump_type;
    }

    public String getDistance() {
        return distance;
    }

    public String getTickrate() {
        return tickrate;
    }

    public String getMsl_count() {
        return msl_count;
    }

    public String getStrafe_count() {
        return strafe_count;
    }

    public String getIs_crouch_bind() {
        return is_crouch_bind;
    }

    public String getIs_forward_bind() {
        return is_forward_bind;
    }

    public String getIs_crouch_boost() {
        return is_crouch_boost;
    }

    public String getUpdated_by_id() {
        return updated_by_id;
    }
}
