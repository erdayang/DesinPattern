package com.mu.yang.observer;

import com.alibaba.fastjson.JSON;

/**
 * Created by xuanda007 on 2016/11/26.
 */
public class Test {
    public static void main(String [] args){
        RecallResult recallResult = RecallResult.getInstance();

        recallResult.addTag(1l, RecallSource.SourceType.SKUREL);
        recallResult.addTag(1l, RecallSource.SourceType.SKUSIM);
        recallResult.addTag(2l, RecallSource.SourceType.COLDSTART);
        recallResult.addTag(2l, RecallSource.SourceType.SKUSIM);

        recallResult.addTag(3l, RecallSource.SourceType.EMPTY);

        System.out.println(JSON.toJSONString(recallResult));

        for(SkuTag skuTag : recallResult.getRecallSource(RecallSource.SourceType.SKUSIM).getSkuTags()){
//            StringBuilder builder = new StringBuilder();
//            builder.append("sku=").append(skuTag.getSku())
//                    .append(", source=").append(skuTag.getSourceTypes());
//            System.out.println(builder.toString());
            System.out.println(skuTag);

        }

        recallResult.removeTag(2l);
    }
}
