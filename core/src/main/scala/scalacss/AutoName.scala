package scalacss

import scala.language.experimental.macros
import scala.reflect.macros.blackbox
import scalacss.DslBase.ToStyle
import scalacss.mutable.Register

object AutoName {
  def forStyle(con: blackbox.Context)(t: con.Expr[ToStyle]*)(c: con.Expr[Compose], register: con.Expr[Register]): con.Expr[StyleA] = {
    import con.universe._

    val fullName = getName(con)
    //register registerS Dsl.style(className)(t: _*)
    con.Expr[StyleA](
      q"""register registerS Dsl.style($fullName.replaceAll("[^\\w-]", "_"))(..$t)"""
    )
  }

  private def getName(c: blackbox.Context): c.Expr[String] = {
    import c.universe._

    val name = c.internal.enclosingOwner.fullName.split("\\[")(0)
    c.Expr[String](q"$name")
  }
}
