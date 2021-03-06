package scalacss

import utest._
import TestUtil._

object DefaultsTest extends TestSuite {

  object DevDefaults extends Defaults {
    override def devMode = true
  }

  object ProdDefaults extends Defaults {
    override def devMode = false
  }

  override val tests = TestSuite {
    'dev  - Dev .test()
    'prod - Prod.test()
  }

  class SharedStyles(implicit reg: mutable.Register) extends mutable.StyleSheet.Inline {
    import dsl._
    implicit def compose = Compose.trust
    val header = style(backgroundColor("#333"))
    val footer = style(backgroundColor("#666"))
  }

  // ===================================================================================================================
  object Dev {
    import DevDefaults._

    object SS extends StyleSheet.Inline {
      import dsl._
      val style1 = style(
        margin(12 px),
        marginLeft(6 px)
      )
      val style2 = style(
        cursor.pointer,
        cursor.zoomIn
      )
      val shared = new SharedStyles
    }

    implicit def env = Env.empty
    val css = SS.render

    def norm(css: String) = css.trim

    def test(): Unit =
      assertEq(norm(css), norm(
        """
          |.DefaultsTest_Dev_SS-0001 {
          |  margin: 12px;
          |  margin-left: 6px;
          |}
          |
          |.DefaultsTest_Dev_SS-0002 {
          |  cursor: pointer;
          |  cursor: -webkit-zoom-in;
          |  cursor: -moz-zoom-in;
          |  cursor: -o-zoom-in;
          |  cursor: zoom-in;
          |}
          |
          |.DefaultsTest_SharedStyles-0003 {
          |  background-color: #333;
          |}
          |
          |.DefaultsTest_SharedStyles-0004 {
          |  background-color: #666;
          |}
        """.stripMargin))
  }

  // ===================================================================================================================
  object Prod {
    import ProdDefaults._

    object SS1 extends StyleSheet.Inline {
      import dsl._
      val style1 = style(
        margin(12 px),
        marginLeft(6 px)
      )
      val style2 = style(
        cursor.pointer,
        cursor.zoomIn
      )
      val shared = new SharedStyles
    }

    object SS2 extends StyleSheet.Inline {
      import dsl._
      val blah = style(width.inherit)
    }

    implicit def env = Env.empty
    val css1 = SS1.render
    val css2 = SS2.render

    def test(): Unit =
      assertEq(css1 +  css2,
        "._a0{margin:12px;margin-left:6px}" +
        "._a1{cursor:pointer;cursor:-webkit-zoom-in;cursor:-moz-zoom-in;cursor:-o-zoom-in;cursor:zoom-in}" +
        "._a2{background-color:#333}" +
        "._a3{background-color:#666}" +
        "._b0{width:inherit}"
      )
  }
}
