package scalabank.database

import scala.collection.mutable.Map as MutableMap

trait HasCache:
  def clearCache(): Unit

abstract class AbstractCache[T, I] extends HasCache:
  protected val cache: MutableMap[I, T] = MutableMap()
  override def clearCache(): Unit = cache.clear()
  
