package com.mu.yang.observer;

import com.alibaba.fastjson.JSON;

import java.util.*;

/**
 * SkuTag 与 RecallSource 形成简单的观察者模式
 *
 * Created by xuanda007 on 2016/11/26.
 */
public class SkuTag {
    private final long sku;
    private transient Map<RecallSource.SourceType, RecallSource> recallSourceMap = new HashMap<RecallSource.SourceType, RecallSource>();

    private Set<RecallSource.SourceType> sources = recallSourceMap.keySet();
    public SkuTag(long sku){
        this.sku = sku;
    }
    public SkuTag(long sku, RecallSource recallSource){
        this.sku = sku;
        addRecallSource(recallSource);
    }

    public Long getSku(){
        return sku;
    }

    public Set<RecallSource.SourceType> getSourceTypes(){
        return recallSourceMap.keySet();
    }

    public void addRecallSource(RecallSource recallSource){
        recallSourceMap.put(recallSource.type, recallSource);  // 有可能会覆盖
        recallSource.addTag(this);
    }

    /**
     * remove this tag from the system
     */
    public void remove(){
        for(Map.Entry<RecallSource.SourceType, RecallSource> entry : recallSourceMap.entrySet()){
            entry.getValue().removeTag(this);
        }
        Iterator<RecallSource.SourceType> iterator = new HashSet<RecallSource.SourceType>(getSourceTypes()).iterator();
        while(iterator.hasNext()){
            recallSourceMap.remove(iterator.next());
            iterator.remove();
        }
    }

    public String toString(){
        return JSON.toJSONString(this);
    }


}
