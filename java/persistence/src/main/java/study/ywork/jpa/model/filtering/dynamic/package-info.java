@org.hibernate.annotations.FilterDef(
        name = "limitByUserRank",
        parameters = {
                @org.hibernate.annotations.ParamDef(
                        name = "currentUserRank", type = "int"
                )
        }
)
@org.hibernate.annotations.FilterDef(
        name = "limitByUserRankDefault",
        defaultCondition =
                ":currentUserRank >= (" +
                        "select u.RANK from USERS u " +
                        "where u.ID = SELLER_ID" +
                        ")",
        parameters = {
                @org.hibernate.annotations.ParamDef(
                        name = "currentUserRank", type = "int"
                )
        }
)
package study.ywork.jpa.model.filtering.dynamic;
