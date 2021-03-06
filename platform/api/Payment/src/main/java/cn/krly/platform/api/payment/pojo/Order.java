package cn.krly.platform.api.payment.pojo;

import java.io.Serializable;
import java.util.Date;

public class Order implements Serializable {
    public static final int PAID = 0;
    public static final int UNPAID = -1;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column order.id
     *
     * @mbggenerated Thu Apr 26 11:22:09 CST 2018
     */
    private Integer id;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column order.user_id
     *
     * @mbggenerated Thu Apr 26 11:22:09 CST 2018
     */
    private Integer userId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column order.project_id
     *
     * @mbggenerated Thu Apr 26 11:22:09 CST 2018
     */
    private Integer projectId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column order.product_id
     *
     * @mbggenerated Thu Apr 26 11:22:09 CST 2018
     */
    private Integer productId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column order.cents
     *
     * @mbggenerated Thu Apr 26 11:22:09 CST 2018
     */
    private Integer cents;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column order.status
     *
     * @mbggenerated Thu Apr 26 11:22:09 CST 2018
     */
    private Integer status;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column order.token
     *
     * @mbggenerated Thu Apr 26 11:22:09 CST 2018
     */
    private String token;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column order.create_time
     *
     * @mbggenerated Thu Apr 26 11:22:09 CST 2018
     */
    private Date createTime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column order.description
     *
     * @mbggenerated Thu Apr 26 11:22:09 CST 2018
     */
    private String description;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column order.id
     *
     * @return the value of order.id
     *
     * @mbggenerated Thu Apr 26 11:22:09 CST 2018
     */
    public Integer getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column order.id
     *
     * @param id the value for order.id
     *
     * @mbggenerated Thu Apr 26 11:22:09 CST 2018
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column order.user_id
     *
     * @return the value of order.user_id
     *
     * @mbggenerated Thu Apr 26 11:22:09 CST 2018
     */
    public Integer getUserId() {
        return userId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column order.user_id
     *
     * @param userId the value for order.user_id
     *
     * @mbggenerated Thu Apr 26 11:22:09 CST 2018
     */
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column order.project_id
     *
     * @return the value of order.project_id
     *
     * @mbggenerated Thu Apr 26 11:22:09 CST 2018
     */
    public Integer getProjectId() {
        return projectId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column order.project_id
     *
     * @param projectId the value for order.project_id
     *
     * @mbggenerated Thu Apr 26 11:22:09 CST 2018
     */
    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column order.product_id
     *
     * @return the value of order.product_id
     *
     * @mbggenerated Thu Apr 26 11:22:09 CST 2018
     */
    public Integer getProductId() {
        return productId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column order.product_id
     *
     * @param productId the value for order.product_id
     *
     * @mbggenerated Thu Apr 26 11:22:09 CST 2018
     */
    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column order.cents
     *
     * @return the value of order.cents
     *
     * @mbggenerated Thu Apr 26 11:22:09 CST 2018
     */
    public Integer getCents() {
        return cents;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column order.cents
     *
     * @param cents the value for order.cents
     *
     * @mbggenerated Thu Apr 26 11:22:09 CST 2018
     */
    public void setCents(Integer cents) {
        this.cents = cents;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column order.status
     *
     * @return the value of order.status
     *
     * @mbggenerated Thu Apr 26 11:22:09 CST 2018
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column order.status
     *
     * @param status the value for order.status
     *
     * @mbggenerated Thu Apr 26 11:22:09 CST 2018
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column order.token
     *
     * @return the value of order.token
     *
     * @mbggenerated Thu Apr 26 11:22:09 CST 2018
     */
    public String getToken() {
        return token;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column order.token
     *
     * @param token the value for order.token
     *
     * @mbggenerated Thu Apr 26 11:22:09 CST 2018
     */
    public void setToken(String token) {
        this.token = token;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column order.create_time
     *
     * @return the value of order.create_time
     *
     * @mbggenerated Thu Apr 26 11:22:09 CST 2018
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column order.create_time
     *
     * @param createTime the value for order.create_time
     *
     * @mbggenerated Thu Apr 26 11:22:09 CST 2018
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column order.description
     *
     * @return the value of order.description
     *
     * @mbggenerated Thu Apr 26 11:22:09 CST 2018
     */
    public String getDescription() {
        return description;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column order.description
     *
     * @param description the value for order.description
     *
     * @mbggenerated Thu Apr 26 11:22:09 CST 2018
     */
    public void setDescription(String description) {
        this.description = description;
    }
}