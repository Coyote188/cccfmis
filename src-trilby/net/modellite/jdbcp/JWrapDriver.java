package net.modellite.jdbcp;

public interface JWrapDriver
{
	public boolean accepts(Class<?> local, Class<?> jdbc);

	public <T> T wrap(Object value, Class<T> clz)
		throws ClassCastException;
}
