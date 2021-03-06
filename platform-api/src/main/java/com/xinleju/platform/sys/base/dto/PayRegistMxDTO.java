package com.xinleju.platform.sys.base.dto;

import com.xinleju.platform.base.dto.BaseDto;

/**
 * @author Jack
 *
 */
public class PayRegistMxDTO extends BaseDto {
	
	//付款登记表外键
	private String payregistid;
	//支付方式
	private String vpayway;
	//付款银行
	private String vpaybank;
	//结算方式
	private String vclearway;
	//结算单号
	private String vclearcode;
	//票据类型
	private String vinvoicetype;
	//票据编号
	private String vinvoicecode;
	//发票金额
	private Double vinvoicemny;
	//付款金额
	private Double npaymny;
	//支付日期
	private String dpaydate;
	//上传凭证字段
	private String issendvoucher;//是否需要生成凭证  ,需要,不需要
	private Integer voucherid;//凭证id
	private String voucherword;//凭证字
	private String vouchernum;//凭证编号
	private String voucherstate;//凭证状态 是,否
	private String vouchersbway;//凭证失败原因
	
	//资金平台同步方法,返回结果中资金平台的唯一标识
	private String payformid;
	
	//预留字段
	private String reverse1;
	private String reverse2;
	private String reverse3;
	private String reverse4;
	private String reverse5;
	private Double reverse6;
	private Double reverse7;
	private Double reverse8;
	private Double reverse9;
	private Double reverse10;
	private String reverse11;
	private String reverse12;
	private String reverse13;
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "PayRegistMxDTO [payregistid=" + payregistid + ", vpayway="
				+ vpayway + ", vpaybank=" + vpaybank + ", vclearway="
				+ vclearway + ", vclearcode=" + vclearcode + ", vinvoicetype="
				+ vinvoicetype + ", vinvoicecode=" + vinvoicecode
				+ ", vinvoicemny=" + vinvoicemny + ", npaymny=" + npaymny
				+ ", dpaydate=" + dpaydate + "]";
	}
	public String getPayregistid() {
		return payregistid;
	}
	public void setPayregistid(String payregistid) {
		this.payregistid = payregistid;
	}
	/**
	 * 123
	 */
	public String getVpayway() {
		return vpayway;
	}
	/**
	 * @param vpayway the vpayway to set
	 */
	public void setVpayway(String vpayway) {
		this.vpayway = vpayway;
	}
	/**
	 * 123
	 */
	public String getVpaybank() {
		return vpaybank;
	}
	/**
	 * @param vpaybank the vpaybank to set
	 */
	public void setVpaybank(String vpaybank) {
		this.vpaybank = vpaybank;
	}
	/**
	 * 123
	 */
	public String getVclearway() {
		return vclearway;
	}
	/**
	 * @param vclearway the vclearway to set
	 */
	public void setVclearway(String vclearway) {
		this.vclearway = vclearway;
	}
	/**
	 * 123
	 */
	public String getVclearcode() {
		return vclearcode;
	}
	/**
	 * @param vclearcode the vclearcode to set
	 */
	public void setVclearcode(String vclearcode) {
		this.vclearcode = vclearcode;
	}
	/**
	 * 123
	 */
	public String getVinvoicetype() {
		return vinvoicetype;
	}
	/**
	 * @param vinvoicetype the vinvoicetype to set
	 */
	public void setVinvoicetype(String vinvoicetype) {
		this.vinvoicetype = vinvoicetype;
	}
	/**
	 * 123
	 */
	public String getVinvoicecode() {
		return vinvoicecode;
	}
	/**
	 * @param vinvoicecode the vinvoicecode to set
	 */
	public void setVinvoicecode(String vinvoicecode) {
		this.vinvoicecode = vinvoicecode;
	}
	/**
	 * 123
	 */
	public Double getVinvoicemny() {
		return vinvoicemny;
	}
	/**
	 * @param vinvoicemny the vinvoicemny to set
	 */
	public void setVinvoicemny(Double vinvoicemny) {
		this.vinvoicemny = vinvoicemny;
	}
	/**
	 * 123
	 */
	public Double getNpaymny() {
		return npaymny;
	}
	/**
	 * @param npaymny the npaymny to set
	 */
	public void setNpaymny(Double npaymny) {
		this.npaymny = npaymny;
	}
	/**
	 * 123
	 */
	public String getDpaydate() {
		return dpaydate;
	}
	/**
	 * @param dpaydate the dpaydate to set
	 */
	public void setDpaydate(String dpaydate) {
		this.dpaydate = dpaydate;
	}
	/**
	 * 123
	 */
	public String getReverse1() {
		return reverse1;
	}
	/**
	 * @param reverse1 the reverse1 to set
	 */
	public void setReverse1(String reverse1) {
		this.reverse1 = reverse1;
	}
	/**
	 * 123
	 */
	public String getReverse2() {
		return reverse2;
	}
	/**
	 * @param reverse2 the reverse2 to set
	 */
	public void setReverse2(String reverse2) {
		this.reverse2 = reverse2;
	}
	/**
	 * 123
	 */
	public String getReverse3() {
		return reverse3;
	}
	/**
	 * @param reverse3 the reverse3 to set
	 */
	public void setReverse3(String reverse3) {
		this.reverse3 = reverse3;
	}
	/**
	 * 123
	 */
	public String getReverse4() {
		return reverse4;
	}
	/**
	 * @param reverse4 the reverse4 to set
	 */
	public void setReverse4(String reverse4) {
		this.reverse4 = reverse4;
	}
	/**
	 * 123
	 */
	public String getReverse5() {
		return reverse5;
	}
	/**
	 * @param reverse5 the reverse5 to set
	 */
	public void setReverse5(String reverse5) {
		this.reverse5 = reverse5;
	}
	/**
	 * 123
	 */
	public Double getReverse6() {
		return reverse6;
	}
	/**
	 * @param reverse6 the reverse6 to set
	 */
	public void setReverse6(Double reverse6) {
		this.reverse6 = reverse6;
	}
	/**
	 * 123
	 */
	public Double getReverse7() {
		return reverse7;
	}
	/**
	 * @param reverse7 the reverse7 to set
	 */
	public void setReverse7(Double reverse7) {
		this.reverse7 = reverse7;
	}
	/**
	 * 123
	 */
	public Double getReverse8() {
		return reverse8;
	}
	/**
	 * @param reverse8 the reverse8 to set
	 */
	public void setReverse8(Double reverse8) {
		this.reverse8 = reverse8;
	}
	/**
	 * 123
	 */
	public Double getReverse9() {
		return reverse9;
	}
	/**
	 * @param reverse9 the reverse9 to set
	 */
	public void setReverse9(Double reverse9) {
		this.reverse9 = reverse9;
	}
	/**
	 * 123
	 */
	public Double getReverse10() {
		return reverse10;
	}
	/**
	 * @param reverse10 the reverse10 to set
	 */
	public void setReverse10(Double reverse10) {
		this.reverse10 = reverse10;
	}
	/**
	 * 123
	 */
	public String getReverse11() {
		return reverse11;
	}
	/**
	 * @param reverse11 the reverse11 to set
	 */
	public void setReverse11(String reverse11) {
		this.reverse11 = reverse11;
	}
	/**
	 * 123
	 */
	public String getReverse12() {
		return reverse12;
	}
	/**
	 * @param reverse12 the reverse12 to set
	 */
	public void setReverse12(String reverse12) {
		this.reverse12 = reverse12;
	}
	/**
	 * 123
	 */
	public String getReverse13() {
		return reverse13;
	}
	/**
	 * @param reverse13 the reverse13 to set
	 */
	public void setReverse13(String reverse13) {
		this.reverse13 = reverse13;
	}
	/**
	 * 123
	 */
	public String getIssendvoucher() {
		return issendvoucher;
	}
	/**
	 * @param issendvoucher the issendvoucher to set
	 */
	public void setIssendvoucher(String issendvoucher) {
		this.issendvoucher = issendvoucher;
	}
	/**
	 * 123
	 */
	public Integer getVoucherid() {
		return voucherid;
	}
	/**
	 * @param voucherid the voucherid to set
	 */
	public void setVoucherid(Integer voucherid) {
		this.voucherid = voucherid;
	}
	/**
	 * 123
	 */
	public String getVoucherword() {
		return voucherword;
	}
	/**
	 * @param voucherword the voucherword to set
	 */
	public void setVoucherword(String voucherword) {
		this.voucherword = voucherword;
	}
	/**
	 * 123
	 */
	public String getVouchernum() {
		return vouchernum;
	}
	/**
	 * @param vouchernum the vouchernum to set
	 */
	public void setVouchernum(String vouchernum) {
		this.vouchernum = vouchernum;
	}
	/**
	 * 123
	 */
	public String getVoucherstate() {
		return voucherstate;
	}
	/**
	 * @param voucherstate the voucherstate to set
	 */
	public void setVoucherstate(String voucherstate) {
		this.voucherstate = voucherstate;
	}
	/**
	 * 123
	 */
	public String getVouchersbway() {
		return vouchersbway;
	}
	/**
	 * @param vouchersbway the vouchersbway to set
	 */
	public void setVouchersbway(String vouchersbway) {
		this.vouchersbway = vouchersbway;
	}
	/**
	 * 123
	 */
	public String getPayformid() {
		return payformid;
	}
	/**
	 * @param payformid the payformid to set
	 */
	public void setPayformid(String payformid) {
		this.payformid = payformid;
	}
	
}
