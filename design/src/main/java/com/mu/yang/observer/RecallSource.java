package com.mu.yang.observer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by xuanda007 on 2016/11/26.
 */
public class RecallSource {

    public final SourceType type ;
    private Map<Long, SkuTag> skuTagMap = new HashMap<Long, SkuTag>();
    private RecallResult recallResult = null;

    public RecallSource(SourceType type){
        this.type = type;
    }

    public RecallSource(SourceType type, RecallResult recallResult){
        this.type = type;
        this.recallResult = recallResult;
    }

    public void addTag(SkuTag skuTag){
        skuTagMap.put(skuTag.getSku(), skuTag);
    }

    public void removeTag(SkuTag skuTag){
        if(skuTag.getSourceTypes().contains(type)){
            skuTagMap.remove(skuTag.getSku());
            recallResult.removeRecallSource(type);
        }
    }

    public List<SkuTag> getSkuTags(){
        return new ArrayList<SkuTag>(skuTagMap.values());
    }

    public int size(){
        return skuTagMap.size();
    }

    public enum SourceType{
        SKUSIM,SKUREL,COLDSTART,EMPTY
    }
}
