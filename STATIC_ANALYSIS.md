# 静态分析工具使用指南

本项目集成了多个Java静态分析工具，帮助提高代码质量和发现潜在问题。

## 可用的静态分析工具

- **Checkstyle**: 代码风格检查
- **PMD**: 代码质量和最佳实践检查  
- **SpotBugs**: Bug检测和安全漏洞检查

## 静态分析命令

### 1. 宽松静态分析 (日常开发使用)
```bash
gradle staticAnalysisLight
```
- 运行所有主要代码的静态分析
- 不会因为检查失败而中断构建
- 适合日常开发时使用

### 2. 代码格式化
```bash
# 检查代码格式问题
gradle spotlessCheck

# 自动修复代码格式
gradle spotlessApply

# 格式化代码并运行静态分析
gradle formatAndAnalyze
```

### 3. 严格静态分析 (发布前检查)
```bash
gradle staticAnalysisStrict
```
- 运行主代码和测试代码的完整静态分析
- 如果发现问题会导致构建失败
- 适合发布前的质量检查

### 3. 生成静态分析报告
```bash
gradle staticAnalysisReport
```
- 运行分析并生成汇总报告
- 报告位置: `app/build/reports/static-analysis/summary.txt`

### 4. 清理静态分析报告
```bash
gradle cleanStaticAnalysis
```
- 清理所有静态分析生成的报告文件

## 单独运行各工具

### Checkstyle
```bash
# 检查主代码
gradle checkstyleMain

# 检查测试代码  
gradle checkstyleTest
```

### PMD
```bash
# 检查主代码
gradle pmdMain

# 检查测试代码
gradle pmdTest
```

### SpotBugs
```bash
# 检查主代码
gradle spotbugsMain

# 检查测试代码
gradle spotbugsTest
```

## 报告位置

所有分析报告都会生成在 `app/build/reports/` 目录下:

- Checkstyle: `app/build/reports/checkstyle/`
- PMD: `app/build/reports/pmd/`
- SpotBugs: `app/build/reports/spotbugs/`

## 配置文件

静态分析工具的配置文件位于 `config/` 目录:

- Checkstyle配置: `config/checkstyle/checkstyle.xml`
- PMD规则: `config/pmd/pmd-rules.xml`
- SpotBugs使用默认配置，可以通过build.gradle调整

## 自定义配置

你可以根据项目需要修改配置文件:

1. **调整Checkstyle规则**: 编辑 `config/checkstyle/checkstyle.xml`
2. **调整PMD规则**: 编辑 `config/pmd/pmd-rules.xml`  
3. **调整SpotBugs设置**: 修改 `app/build.gradle` 中的spotbugs配置块

## 集成到CI/CD

建议在CI/CD流水线中使用:
- 开发分支: `gradle staticAnalysisLight`
- 主分支/发布: `gradle staticAnalysisStrict`
