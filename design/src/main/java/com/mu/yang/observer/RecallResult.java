package com.mu.yang.observer;

import java.util.*;

/**
 * 某一个sku的来源有很多，用此方法记录下来
 *
 * Created by xuanda007 on 2016/11/26.
 */
public class RecallResult {

    private Map<Long, SkuTag> skuTagMap = new HashMap<Long, SkuTag>();

    private Map<RecallSource.SourceType, RecallSource> recallSourceMap = new HashMap<RecallSource.SourceType, RecallSource>();

    private transient static RecallResult recallResult;
    private RecallResult(){}
    public static RecallResult getInstance(){
        synchronized (RecallResult.class){
            if(null == recallResult){
                synchronized (RecallResult.class){
                    recallResult = new RecallResult();
                }
            }
        }
        return recallResult;
    }

    /**
     * 添加tag
     * @param sku
     * @param type
     */
    public void addTag(Long sku, RecallSource.SourceType type){
        RecallSource recallSource = getRecallSource(type);
        SkuTag tag = getSkuTag(sku);
        if(tag == null){
            tag = new SkuTag(sku, recallSource);
        }else{
            tag.addRecallSource(recallSource);
        }
        skuTagMap.put(sku, tag);
    }

    /**
     * 移除tag
     * @param sku
     */
    public void removeTag(Long sku){
        SkuTag tag = getSkuTag(sku);
        if(null == tag) return;
        tag.remove();
        skuTagMap.remove(sku);
    }

    public SkuTag getSkuTag(Long sku){
        return skuTagMap.get(sku);
    }

    public RecallSource getRecallSource(RecallSource.SourceType type){
        if(! recallSourceMap.containsKey(type)){
            RecallSource recallSource = new RecallSource(type, this);
            recallSourceMap.put(type, recallSource);
        }
        return recallSourceMap.get(type);
    }

    public void removeRecallSource(RecallSource.SourceType type){
        if( recallSourceMap.containsKey(type)){
           if(recallSourceMap.get(type).size() == 0){
                recallSourceMap.remove(type);
           }
        }
    }

    public Set<RecallSource.SourceType> getSourceTypes(){
        return recallSourceMap.keySet();
    }
}
