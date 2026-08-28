# CI/CD Demo — GitHub Actions ile Test Otomasyonu Pipeline'ı

Her `push` / `pull_request`'te otomatik olarak UI ve API testlerini koşan örnek bir CI pipeline'ı.

## İçerik
- **UI testi:** Selenium (Java/Maven, JUnit 5), headless Chrome — yerel bir HTML sayfasını doğrular.
- **API testi:** Postman koleksiyonu, Newman ile koşulur — status/şema/iş kuralı/performans assertion'ları.
- **Pipeline:** `.github/workflows/ci.yml` — iki paralel job (`ui-tests`, `api-tests`), test raporları artifact olarak saklanır.

## Yerelde çalıştırma
```bash
mvn test                                              # Selenium testleri
newman run api/demo-collection.postman_collection.json # API testleri
```

## Pipeline nasıl çalışır?
| Kavram | Bu projede karşılığı |
|--------|----------------------|
| Trigger (`on:`) | `push`, `pull_request`, `workflow_dispatch` (elle) |
| Runner (`runs-on:`) | `ubuntu-latest` (GitHub sağlar, Chrome kurulu gelir) |
| Job | `ui-tests` ve `api-tests` (paralel koşar) |
| Step | checkout → JDK/Node kur → test koş → rapor yükle |
| Artifact | `surefire-report`, `newman-report` (koşu sonrası indirilebilir) |

## Mülakatta anlatım notları
- **"Testi pipeline-ready yapmak"**: headless mod, `-B` batch, exit code'a göre build'in kırılması, JUnit XML rapor çıktısı.
- **Katmanlı tetikleme**: hızlı testler her push'ta, ağır regresyon `schedule` ile nightly (bu repoda trigger'a `schedule` eklenerek gösterilebilir).
- **Flaky ders**: performans assertion eşiği çok sıkı tutulursa (ör. 2sn) ortam kaynaklı patlar; gerçekçi eşik + izolasyon.
- **`if: always()`**: test patlasa bile raporun saklanması — hata ayıklama için kritik.
