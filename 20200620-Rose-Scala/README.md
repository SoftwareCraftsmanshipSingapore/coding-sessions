test command

```
diff <(sbt --error "test:run 16") baseline.text && echo OK
```