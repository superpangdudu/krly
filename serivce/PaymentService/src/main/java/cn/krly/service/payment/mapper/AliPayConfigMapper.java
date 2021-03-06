package cn.krly.service.payment.mapper;

import cn.krly.platform.api.payment.pojo.AliPayConfig;

public interface AliPayConfigMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table alipay_config
     *
     * @mbggenerated Fri Apr 27 17:17:24 CST 2018
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table alipay_config
     *
     * @mbggenerated Fri Apr 27 17:17:24 CST 2018
     */
    int insert(AliPayConfig record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table alipay_config
     *
     * @mbggenerated Fri Apr 27 17:17:24 CST 2018
     */
    int insertSelective(AliPayConfig record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table alipay_config
     *
     * @mbggenerated Fri Apr 27 17:17:24 CST 2018
     */
    AliPayConfig selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table alipay_config
     *
     * @mbggenerated Fri Apr 27 17:17:24 CST 2018
     */
    int updateByPrimaryKeySelective(AliPayConfig record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table alipay_config
     *
     * @mbggenerated Fri Apr 27 17:17:24 CST 2018
     */
    int updateByPrimaryKey(AliPayConfig record);

    AliPayConfig getAliPayConfig(int projectId);
    int deleteAliPayConfig(int projectId);
}