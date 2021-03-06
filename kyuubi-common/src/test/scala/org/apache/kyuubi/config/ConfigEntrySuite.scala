/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.kyuubi.config

import org.apache.kyuubi.KyuubiFunSuite

class ConfigEntrySuite extends KyuubiFunSuite {

  test("optional config entry") {
    val e1 = new OptionalConfigEntry[Int](
      "kyuubi.int.spark",
      s => s.toInt + 1,
      v => (v - 1).toString,
      "this is dummy documentation",
      "<none>")

    val conf = KyuubiConf()
    assert(conf.get(e1).isEmpty)
    val e = intercept[IllegalArgumentException](new OptionalConfigEntry[Int](
      "kyuubi.int.spark",
      s => s.toInt + 1,
      v => (v - 1).toString,
      "this is dummy documentation",
      "<none>"))
    assert(e.getMessage ===
      "requirement failed: Config entry kyuubi.int.spark already registered!")
    conf.set(e1.key, "2")
    assert(conf.get(e1).get === 3)

  }

  test("config entry with default") {
    val e1 = new ConfigEntryWithDefault[Long]("kyuubi.long.spark",
      2,
      s => s.toLong + 1,
      v => (v - 1).toString,
    "",
    "")
    val conf = KyuubiConf()
    assert(conf.get(e1) === 2)
    conf.set(e1.key, "5")
    assert(conf.get(e1) === 6)
  }

  test("config entry with default string") {
    val e1 = new ConfigEntryWithDefaultString[Double](
      "kyuubi.double.spark",
      "3.0", s => java.lang.Double.valueOf(s),
      v => v.toString,
      "",
      "")
    val conf = KyuubiConf()
    assert(conf.get(e1) === 3.0)
    conf.set(e1.asInstanceOf[ConfigEntry[AnyVal]], 5.0)
    assert(conf.get(e1) === 5.0)
  }

}
