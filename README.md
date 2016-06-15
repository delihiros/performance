# performance

`into-mapper` seems to be the fastest

|                                      :name |  :n |  :sum |  :q1 | :med |  :q3 |   :sd |  :mad |
|--------------------------------------------|-----|-------|------|------|------|-------|-------|
|             #'performance.core/into-mapper | 100 |   3ms | 13µs | 14µs | 15µs | 115µs |   1µs |
|           #'performance.core/reduce-mapper | 100 | 638ms |  5ms |  5ms |  6ms |   2ms | 109µs |
| #'performance.core/reduce-mapper-transient | 100 | 224ms |  2ms |  2ms |  2ms | 470µs |  43µs |
|              #'performance.core/for-mapper | 100 | 973ms |  9ms |  9ms |  9ms |   3ms | 143µs |
