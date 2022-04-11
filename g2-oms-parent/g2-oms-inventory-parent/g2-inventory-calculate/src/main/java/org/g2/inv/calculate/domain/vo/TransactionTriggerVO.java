package org.g2.inv.calculate.domain.vo;

import java.util.Objects;

/**
 * 库存事务触发信息
 *
 * @author wuwenxi 2022-04-08
 */
public class TransactionTriggerVO {

    /**
     * 服务点编码
     */
    private String posCode;
    /**
     * master sku 编码
     */
    private String masterSkuCode;

    public String getPosCode() {
        return posCode;
    }

    public void setPosCode(String posCode) {
        this.posCode = posCode;
    }

    public String getMasterSkuCode() {
        return masterSkuCode;
    }

    public void setMasterSkuCode(String masterSkuCode) {
        this.masterSkuCode = masterSkuCode;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || getClass() != object.getClass()) {
            return false;
        }
        TransactionTriggerVO that = (TransactionTriggerVO) object;
        return Objects.equals(posCode, that.posCode) &&
                Objects.equals(masterSkuCode, that.masterSkuCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(posCode, masterSkuCode);
    }

    @Override
    public String toString() {
        return "TransactionTriggerVO{" +
                "posCode='" + posCode + '\'' +
                ", masterSkuCode='" + masterSkuCode + '\'' +
                '}';
    }
}
