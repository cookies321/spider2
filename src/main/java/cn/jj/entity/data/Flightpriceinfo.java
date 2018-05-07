package cn.jj.entity.data;

import java.io.Serializable;
import java.util.Date;

public class Flightpriceinfo implements Serializable{
    private static final long serialVersionUID = 1L;

	private String id;

    private String flightid;

    private String flightno;

    private String flightdate;

    private String classtype;

    private String identitytype;

    private String price;

    private String discount;

    private String constructionfee;

    private String oilfee;

    private String comment;

    private String datasource;

    private Date createdate;

    private String creator;

    private String creatorid;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getFlightid() {
        return flightid;
    }

    public void setFlightid(String flightid) {
        this.flightid = flightid == null ? null : flightid.trim();
    }

    public String getFlightno() {
        return flightno;
    }

    public void setFlightno(String flightno) {
        this.flightno = flightno == null ? null : flightno.trim();
    }

    public String getFlightdate() {
        return flightdate;
    }

    public void setFlightdate(String flightdate) {
        this.flightdate = flightdate == null ? null : flightdate.trim();
    }

    public String getClasstype() {
        return classtype;
    }

    public void setClasstype(String classtype) {
        this.classtype = classtype == null ? null : classtype.trim();
    }

    public String getIdentitytype() {
        return identitytype;
    }

    public void setIdentitytype(String identitytype) {
        this.identitytype = identitytype == null ? null : identitytype.trim();
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price == null ? null : price.trim();
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount == null ? null : discount.trim();
    }

    public String getConstructionfee() {
        return constructionfee;
    }

    public void setConstructionfee(String constructionfee) {
        this.constructionfee = constructionfee == null ? null : constructionfee.trim();
    }

    public String getOilfee() {
        return oilfee;
    }

    public void setOilfee(String oilfee) {
        this.oilfee = oilfee == null ? null : oilfee.trim();
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment == null ? null : comment.trim();
    }

    public String getDatasource() {
        return datasource;
    }

    public void setDatasource(String datasource) {
        this.datasource = datasource == null ? null : datasource.trim();
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
}