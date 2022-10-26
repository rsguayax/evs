package es.grammata.evaluation.evs.data.model.repository;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import es.grammata.evaluation.evs.data.model.base.BaseModelEntity;

@Entity
@Table(name = "CAP")
public class Cap extends BaseModelEntity<Long> {

    private static final long serialVersionUID = 1L;

    @ManyToOne
    @PrimaryKeyJoinColumn
    @NotNull(message = "El campo no puede estar vacío")
    private EvaluationCenter evaluationCenter;

    @NotNull(message = "El campo no puede estar vacío")
    @Column(unique = true)
    private String name;

    @NotNull(message = "El campo no puede estar vacío")
    @Column(unique = true)
    private String serialNumber;

    @NotNull(message = "El campo no puede estar vacío")
    private String ssid;

    @NotNull(message = "El campo no puede estar vacío")
    @Column(unique = true)
    private String key;
    
    @ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="server_id")
    private Server server;
    

    public EvaluationCenter getEvaluationCenter() {
        return evaluationCenter;
    }

    public void setEvaluationCenter(EvaluationCenter evaluationCenter) {
        this.evaluationCenter = evaluationCenter;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getSsid() {
        return ssid;
    }

    public void setSsid(String ssid) {
        this.ssid = ssid;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
    
    public Server getServer() {
		return server;
	}

	public void setServer(Server server) {
		this.server = server;
	}

	@Override
    public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Cap [evaluationCenter=");
		builder.append(evaluationCenter);
		builder.append(", name=");
		builder.append(name);
		builder.append(", serialNumber=");
		builder.append(serialNumber);
		builder.append(", ssid=");
		builder.append(ssid);
		builder.append(", key=");
		builder.append(key);
		builder.append("]");
		return builder.toString();
    }

}
