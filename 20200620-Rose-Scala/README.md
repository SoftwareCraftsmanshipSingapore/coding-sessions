baseline test command

```
diff <(sbt --error "test:run 16") baseline.text && echo OK
```

test command including conjured implementation

```
diff <(sbt --error "test:run 16") baselineAndConjured.text && echo OK
```
