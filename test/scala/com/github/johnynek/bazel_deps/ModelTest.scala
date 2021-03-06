package com.github.johnynek.bazel_deps

import org.scalatest.FunSuite

class ModelTest extends FunSuite {
  test("specific versions sort correctly") {
    assert(VersionConflictPolicy
      .Highest
      .resolve(None, Set(Version("11.0.2"), Version("18.0"))) === Right(Version("18.0")))

    assert(VersionConflictPolicy
      .Highest
      .resolve(Some(Version("18.1")), Set(Version("11.0.2"))) === Right(Version("18.1")))
  }
  test("RC/M is less than final") {
    val ord = Ordering[Version]
    assert(ord.lt(Version("0.5.0-M2"), Version("0.5.0")), "0.5.0-M2 < 0.5.0")
    assert(ord.lt(Version("0.16.1-RC2"), Version("0.16.1")), "0.16.1-RC1 < 0.16.1")
    assert(ord.lt(Version("0.16.1-RC2"), Version("0.16.10-RC4")), "0.16.1-RC1 < 0.16.10-RC4")
    assert(ord.lt(Version("0.16.2-RC2"), Version("0.16.10-RC4")), "0.16.2-RC1 < 0.16.10-RC4")
  }
  test("a test array is sorted") {
    val sorted =
      List("1.0.9a", "1.0.9", "1.0.10", "2.0RC0", "2.0-rc1", "2.0rc2", "2.0", "3.1.4.2-M1", "3.1.4.2", "10.2")
        .map(Version(_))

    val rand = scala.util.Random.shuffle(sorted)
    assert(rand.sorted === sorted)
  }
}
