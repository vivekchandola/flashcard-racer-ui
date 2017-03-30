import javax.inject.Singleton;

import play.http.HttpFilters;
import play.mvc.EssentialFilter;

/**
 * This class configures filters that run on every request. This class is
 * queried by Play to get a list of filters.
 *
 * Play will automatically use filters from any class called
 * <code>Filters</code> that is placed the root package. You can load filters
 * from a different class by adding a `play.http.filters` setting to the
 * <code>application.conf</code> configuration file.
 */
@Singleton
public class Filters implements HttpFilters {
	@Override
	public EssentialFilter[] filters() {
		return new EssentialFilter[] {};
	}
}
