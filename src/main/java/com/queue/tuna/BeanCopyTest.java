package com.queue.tuna;

import net.sf.cglib.beans.BeanCopier;
import org.apache.commons.beanutils.BeanUtils;

import java.lang.reflect.InvocationTargetException;

/**
 * Created by zhangtao on 2017/2/27.
 */
class MobileCardCompanyDO{
    private String CompanyCode;
    private String CompanyDesc;
    private String CompanyName;
    private long id;
    private String status;

    public String getCompanyCode() {
        return CompanyCode;
    }

    public void setCompanyCode(String companyCode) {
        CompanyCode = companyCode;
    }

    public String getCompanyDesc() {
        return CompanyDesc;
    }

    public void setCompanyDesc(String companyDesc) {
        CompanyDesc = companyDesc;
    }

    public String getCompanyName() {
        return CompanyName;
    }

    public void setCompanyName(String companyName) {
        CompanyName = companyName;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
class CompanyModel{
    private String CompanyCode;
    private String CompanyDesc;
    private String CompanyName;
    private long id;
    private String status;

    public String getCompanyCode() {
        return CompanyCode;
    }

    public void setCompanyCode(String companyCode) {
        CompanyCode = companyCode;
    }

    public String getCompanyDesc() {
        return CompanyDesc;
    }

    public void setCompanyDesc(String companyDesc) {
        CompanyDesc = companyDesc;
    }

    public String getCompanyName() {
        return CompanyName;
    }

    public void setCompanyName(String companyName) {
        CompanyName = companyName;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
public class BeanCopyTest {
    public static void main(String[] args) throws InvocationTargetException, IllegalAccessException {
        MobileCardCompanyDO cardPayOrderModel = new MobileCardCompanyDO();
        cardPayOrderModel.setCompanyCode("HS");
        cardPayOrderModel.setCompanyDesc("a1231241241awdasdf");
        cardPayOrderModel.setCompanyName("123124dzvsds");
        cardPayOrderModel.setId(2l);
        cardPayOrderModel.setStatus("1");

        /**
         * 10W次
         * BeanUtils.copyProperties：718
         * beanCopier.copy：56
         */

        /**
         * 100W次
         * BeanUtils.copyProperties：5673
         * beanCopier.copy：70
         */

        //BeanUtils性能
        long start = System.currentTimeMillis();
        for (int i = 0; i < 1000000; i++) {
            CompanyModel companyModel = new CompanyModel();
            NewBeanUtils.copyProperties(cardPayOrderModel, companyModel);
        }
        long end = System.currentTimeMillis();
        System.out.println(end - start);

        //BeanCopier性能
        long start1 = System.currentTimeMillis();
        BeanCopier beanCopier = BeanCopier.create(MobileCardCompanyDO.class, CompanyModel.class,
                false);
        for (int i = 0; i < 1000000; i++) {
            CompanyModel companyModel = new CompanyModel();
            beanCopier.copy(cardPayOrderModel, companyModel, null);
        }
        long end1 = System.currentTimeMillis();
        System.out.println(end1 - start1);

        //对象get、set性能
        long start2 = System.currentTimeMillis();
        for (int i = 0; i < 1000000; i++) {
            CompanyModel companyModel = new CompanyModel();
            companyModel.setCompanyCode(cardPayOrderModel.getCompanyCode());
            companyModel.setCompanyDesc(cardPayOrderModel.getCompanyDesc());
            companyModel.setCompanyName(cardPayOrderModel.getCompanyName());
            companyModel.setId(cardPayOrderModel.getId());
            companyModel.setStatus(cardPayOrderModel.getStatus());
        }
        long end2 = System.currentTimeMillis();

        System.out.println(end2 - start2);
    }
}
