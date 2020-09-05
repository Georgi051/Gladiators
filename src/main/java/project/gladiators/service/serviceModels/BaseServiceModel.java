package project.gladiators.service.serviceModels;

import java.util.Objects;

public class BaseServiceModel {
    private String id;

    protected BaseServiceModel(){
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BaseServiceModel)) return false;
        BaseServiceModel that = (BaseServiceModel) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
