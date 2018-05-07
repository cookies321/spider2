package cn.jj.entity.data;

import java.io.Serializable;
import java.util.Date;

public class Hotelinfo implements Serializable{
    private static final long serialVersionUID = 1L;

	private String id;

    private String url;

    private String name;

    private String address;

    private String longitude;

    private String latitude;

    private String star;

    private String price;

    private String datasource;

    private String grade;

    private String gradenum;

    private String beennum;

    private String whantto;

    private Date createdate;

    private String creator;

    private String creatorid;

    private String remark;

    private String introduction;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address == null ? null : address.trim();
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude == null ? null : longitude.trim();
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude == null ? null : latitude.trim();
    }

    public String getStar() {
        return star;
    }

    public void setStar(String star) {
        this.star = star == null ? null : star.trim();
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price == null ? null : price.trim();
    }

    public String getDatasource() {
        return datasource;
    }

    public void setDatasource(String datasource) {
        this.datasource = datasource == null ? null : datasource.trim();
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade == null ? null : grade.trim();
    }

    public String getGradenum() {
        return gradenum;
    }

    public void setGradenum(String gradenum) {
        this.gradenum = gradenum == null ? null : gradenum.trim();
    }

    public String getBeennum() {
        return beennum;
    }

    public void setBeennum(String beennum) {
        this.beennum = beennum == null ? null : beennum.trim();
    }

    public String getWhantto() {
        return whantto;
    }

    public void setWhantto(String whantto) {
        this.whantto = whantto == null ? null : whantto.trim();
    }

    public Date getCreatedate() {
        return createdate;
    }

    public void setCreatedate(Date createdate) {
        this.createdate = createdate;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator == null ? null : creator.trim();
    }

    public String getCreatorid() {
        return creatorid;
    }

    public void setCreatorid(String creatorid) {
        this.creatorid = creatorid == null ? null : creatorid.trim();
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction == null ? null : introduction.trim();
    }
}