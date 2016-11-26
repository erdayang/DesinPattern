package com.mu.yang.filter;

/**
 * Created by xuanda007 on 2016/11/26.
 */
public abstract class AbstractFilter implements Filter{

    public abstract void doFilter(Context context);

    public void filter(Context context, FilterChain filterChain){
        doFilter(context);
        filterChain.doFilter(context);
    }
}
