package scalabank.database

import scala.collection.mutable.Map as MutableMap

/**
 * A trait representing an entity with a cache that can be cleared.
 */
trait HasCache:
  /**
   * Clears the cache.
   */
  def clearCache(): Unit

/**
 * An abstract class providing a basic cache implementation.
 *
 * @tparam T the type of the values in the cache
 * @tparam I the type of the keys in the cache
 */
abstract class AbstractCache[T, I] extends HasCache:
  protected val cache: MutableMap[I, T] = MutableMap()
  override def clearCache(): Unit = cache.clear()
  
