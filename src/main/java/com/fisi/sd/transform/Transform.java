package com.fisi.sd.transform;

import java.util.List;

/**
 * @param <T1> Model
 * @param <T2> Entity
 */
public interface Transform <T1,T2>{
	public T2 transformME(T1 oModel);
	public List<T2>transformME(List<T1> lModel);
	public T1 transformEM(T2 oEntity);
	public List<T1> transformEM(List<T2> lEntity);
}