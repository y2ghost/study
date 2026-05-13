package study.ywork.jpa.model.inheritance.mappedsuperclass;

import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotNull;

/**
 * 不同的表可以共享字段，为了扩展性，父类（本类）不建议映射具体的数据库表
 * 因为修改本类导致所有子类的字段都需要进行映射更新
 * 参考隐式多态继承映射.png
 */
@MappedSuperclass
public abstract class BillingDetails {
    @NotNull
    protected String owner;

    protected BillingDetails() {
    }

    protected BillingDetails(String owner) {
        this.owner = owner;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }
}
