package com.xinleju.platform.sys.base.utils;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)  
@XmlType(name = "basdoc_head", propOrder = {})
public class BasdocHead {
	@XmlElement(name="pk_corp")
	private String pkcorp;
	@XmlElement(name="pk_areacl")
	private String pkareacl;
	@XmlElement(name="custcode")
	private String custcode;
	@XmlElement(name="custname")
	private String custname;
	@XmlElement(name="custshortname")
	private String custshortname;
	@XmlElement(name="custtype")
	private String custtype;
	@XmlElement(name="custprop")
	private String custprop;
	@XmlElement
	private String linkman1;
	@XmlElement
	private String fax1;
	@XmlElement
	private String mobilephone1;
	@XmlElement
	private String registerfund;
	/**
	 * @return the pkcorp
	 */
	public String getPkcorp() {
		return pkcorp;
	}
	/**
	 * @param pkcorp the pkcorp to set
	 */
	public void setPkcorp(String pkcorp) {
		this.pkcorp = pkcorp;
	}
	/**
	 * @return the pkareacl
	 */
	public String getPkareacl() {
		return pkareacl;
	}
	/**
	 * @param pkareacl the pkareacl to set
	 */
	public void setPkareacl(String pkareacl) {
		this.pkareacl = pkareacl;
	}
	/**
	 * @return the custcode
	 */
	public String getCustcode() {
		return custcode;
	}
	/**
	 * @param custcode the custcode to set
	 */
	public void setCustcode(String custcode) {
		this.custcode = custcode;
	}
	/**
	 * @return the custname
	 */
	public String getCustname() {
		return custname;
	}
	/**
	 * @param custname the custname to set
	 */
	public void setCustname(String custname) {
		this.custname = custname;
	}
	/**
	 * @return the custshortname
	 */
	public String getCustshortname() {
		return custshortname;
	}
	/**
	 * @param custshortname the custshortname to set
	 */
	public void setCustshortname(String custshortname) {
		this.custshortname = custshortname;
	}
	/**
	 * @return the custtype
	 */
	public String getCusttype() {
		return custtype;
	}
	/**
	 * @param custtype the custtype to set
	 */
	public void setCusttype(String custtype) {
		this.custtype = custtype;
	}
	/**
	 * @return the custprop
	 */
	public String getCustprop() {
		return custprop;
	}
	/**
	 * @param custprop the custprop to set
	 */
	public void setCustprop(String custprop) {
		this.custprop = custprop;
	}
	/**
	 * @return the linkman1
	 */
	public String getLinkman1() {
		return linkman1;
	}
	/**
	 * @param linkman1 the linkman1 to set
	 */
	public void setLinkman1(String linkman1) {
		this.linkman1 = linkman1;
	}
	/**
	 * @return the fax1
	 */
	public String getFax1() {
		return fax1;
	}
	/**
	 * @param fax1 the fax1 to set
	 */
	public void setFax1(String fax1) {
		this.fax1 = fax1;
	}
	/**
	 * @return the mobilephone1
	 */
	public String getMobilephone1() {
		return mobilephone1;
	}
	/**
	 * @param mobilephone1 the mobilephone1 to set
	 */
	public void setMobilephone1(String mobilephone1) {
		this.mobilephone1 = mobilephone1;
	}
	/**
	 * @return the registerfund
	 */
	public String getRegisterfund() {
		return registerfund;
	}
	/**
	 * @param registerfund the registerfund to set
	 */
	public void setRegisterfund(String registerfund) {
		this.registerfund = registerfund;
	}

}
