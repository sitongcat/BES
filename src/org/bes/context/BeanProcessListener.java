package org.bes.context;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

@Component
public class BeanProcessListener implements BeanPostProcessor {

	@Override
	public Object postProcessBeforeInitialization(Object o, String s) throws BeansException {
		return o;
	}

	@Override
	public Object postProcessAfterInitialization(Object o, String s) throws BeansException {
		return o;
	}

}
