package scalacss

import utest._

import scalacss.TestUtil._

object AutoNameTest extends TestSuite {

  object Issue35 {
    import DevDefaults._
    object SampleStyles extends StyleSheet.Inline {
      override implicit val classNameHint = ClassNameHint("TEST")
      import dsl._
      val other = styleM(borderCollapse.collapse, &.hover(fontWeight._200), fontWeight._100)
      val outer = style("outer")(fontWeight.bold)
      val inner = style(color.red, outer)
    }
    def testNames(): Unit = {
      assertEq(SampleStyles.other.htmlClass, "scalacss_AutoNameTest_Issue35_SampleStyles_other")
      assertEq(SampleStyles.outer.htmlClass, "outer")
      assertEq(SampleStyles.inner.htmlClass, "TEST-0001")
    }
    def testCss(): Unit = {
      val css = SampleStyles.renderA[String].trim
      assertEq(css,
        """
          |.scalacss_AutoNameTest_Issue35_SampleStyles_other {
          |  border-collapse: collapse;
          |  font-weight: 100;
          |}
          |
          |.scalacss_AutoNameTest_Issue35_SampleStyles_other:hover {
          |  font-weight: 200;
          |}
          |
          |.outer {
          |  font-weight: bold;
          |}
          |
          |.TEST-0001 {
          |  color: red;
          |  font-weight: bold;
          |}
        """.stripMargin.trim)
    }
  }

  override val tests = TestSuite {
    'issue35names - Issue35.testNames()
    'issue35css - Issue35.testCss()
  }
}
