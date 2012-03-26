package de.tototec.sbuild

import org.junit.runner.RunWith
import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class CmvnSchemeHandlerTest extends FunSuite {

  val basePath = "/tmp/cmvn-scheme-test"
  val cmvn = new MvnSchemeHandler(basePath)

  test("local path test 1") {
    assert(cmvn.localPath("a:b:1") === "file:" + basePath + "/a/b/1/b-1.jar")
  }

  test("local path test 2") {
    assert(cmvn.localPath("a:b:1;classifier=opt1") === "file:" + basePath + "/a/b/1/b-1-opt1.jar")
  }

}