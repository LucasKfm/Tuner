# 🎸 Tunner - Afinador de Instrumentos Musicais

Aplicativo Android de afinação de instrumentos musicais (violão, guitarra, etc.) desenvolvido em **Kotlin** com **Jetpack Compose**.

## ✨ Funcionalidades

- 🎵 Detecção automática de nota musical em tempo real
- 📊 Exibição do desvio em cents (-100c a +100c)
- 🎯 Ponteiro curvado tipo velocímetro mostrando precisão da afinação
- 🔄 Scroll horizontal com todas as notas musicais (A0 até G7#)
- 🎨 Feedback visual por cores:
  - **Verde**: Afinado (±5 cents)
  - **Azul**: Um pouco alto
  - **Vermelho**: Um pouco baixo
- 🌙 Interface moderna com tema dark

## 🏗️ Arquitetura

O projeto segue os princípios de **Clean Architecture** com separação em camadas:

### Domain Layer (`domain/`)
- **Models**: `MusicalNote`, `TunerResult`
- **Repository Interface**: `ITunerRepository`
- **Use Cases**: `StartTuningUseCase`, `StopTuningUseCase`

### Data Layer (`data/`)
- **AudioCaptureManager**: Gerencia captura de áudio do microfone
- **PitchDetector**: Detecta frequência usando TarsosDSP
- **TunerRepositoryImpl**: Implementação do repositório

### Presentation Layer (`presentation/`)
- **TunerViewModel**: Gerencia estado da UI usando StateFlow
- **TunerUiState**: Estado da tela do afinador
- **TunerScreen**: UI principal em Compose

## 🛠️ Tecnologias

- **Kotlin** - Linguagem de programação
- **Jetpack Compose** - Framework UI declarativo
- **Material Design 3** - Design system
- **Algoritmo de Autocorrelação** - Detecção de pitch nativa (sem dependências externas)
- **Koin** - Injeção de dependências
- **Coroutines + Flow** - Programação assíncrona
- **ViewModel + StateFlow** - Gerenciamento de estado
- **AudioRecord API** - Captura de áudio do microfone
- **Accompanist Permissions** - Gerenciamento de permissões

## 📋 Pré-requisitos

- Android Studio Hedgehog ou superior
- JDK 11 ou superior
- SDK mínimo: API 28 (Android 9.0)
- SDK alvo: API 36

## 🚀 Como executar

1. Clone o repositório
2. Abra o projeto no Android Studio
3. Aguarde o Gradle sincronizar as dependências
4. Execute o app em um dispositivo físico (recomendado)
5. Conceda a permissão de gravação de áudio
6. O app inicia automaticamente a captura de áudio
7. Toque uma nota no seu instrumento e observe o feedback visual em tempo real

## 📱 Permissões

O aplicativo requer a seguinte permissão:

- `RECORD_AUDIO` - Para capturar áudio do microfone em tempo real

## 📦 Dependências principais

```gradle
implementation "androidx.compose.ui:ui"
implementation "androidx.compose.material3:material3"
implementation "io.insert-koin:koin-android:3.5.0"
implementation "com.google.accompanist:accompanist-permissions:0.32.0"
```

**Nota**: A detecção de pitch é feita com implementação nativa em Kotlin (sem dependências externas de DSP).

## 🎯 Princípios de Design

O projeto segue os seguintes princípios:

- **SOLID**: Separação de responsabilidades e inversão de dependência
- **Clean Architecture**: Separação clara entre camadas
- **KISS (Keep It Simple)**: Código simples e fácil de entender
- **Single Responsibility**: Cada classe/função tem uma única responsabilidade
- **Dependency Injection**: Todas as dependências são injetadas via Koin

## 🔧 Estrutura de Diretórios

```
app/src/main/java/com/app4funbr/tunner/
├── data/
│   ├── audio/
│   │   ├── AudioCaptureManager.kt
│   │   └── PitchDetector.kt
│   └── repository/
│       └── TunerRepositoryImpl.kt
├── domain/
│   ├── model/
│   │   ├── MusicalNote.kt
│   │   └── TunerResult.kt
│   ├── repository/
│   │   └── ITunerRepository.kt
│   └── usecase/
│       ├── StartTuningUseCase.kt
│       └── StopTuningUseCase.kt
├── presentation/
│   └── tuner/
│       ├── components/
│       │   ├── NoteScrollRow.kt
│       │   └── TunerGauge.kt
│       ├── TunerScreen.kt
│       ├── TunerUiState.kt
│       └── TunerViewModel.kt
├── di/
│   ├── DataModule.kt
│   ├── DomainModule.kt
│   ├── PresentationModule.kt
│   └── AppModule.kt
├── ui/theme/
│   ├── Color.kt
│   ├── Theme.kt
│   └── Type.kt
├── MainActivity.kt
└── TunerApplication.kt
```

## 🎵 Como funciona

1. **Captura de Áudio**: O `AudioCaptureManager` usa a API `AudioRecord` para capturar áudio do microfone em buffers de 16-bit PCM a 44.1kHz
2. **Detecção de Pitch**: O `SimplePitchDetector` processa cada buffer usando **algoritmo de autocorrelação** com interpolação parabólica para identificar a frequência fundamental com precisão
3. **Conversão para Nota**: A frequência é convertida para a nota musical mais próxima usando a fórmula: `midiNumber = 12 * log2(frequency / 440) + 69`
4. **Cálculo de Desvio**: O desvio em cents é calculado: `cents = 1200 * log2(detectedFreq / noteFreq)`
5. **Atualização da UI**: O estado flui através de StateFlow para atualizar a interface em tempo real

## 📝 Notas Técnicas

- **Taxa de Amostragem**: 44.1kHz
- **Formato de Áudio**: PCM 16-bit mono
- **Algoritmo de Detecção**: Autocorrelação com interpolação parabólica (implementação nativa)
- **Tamanho do Buffer**: 4096 samples
- **Range de Detecção**: 50Hz a 4000Hz
- **Range de Notas**: A0 (27.5Hz) até G7# (3322Hz)
- **Tolerância de Afinação**: ±5 cents
- **Tamanho do APK**: ~29MB

## 📄 Licença

Este projeto foi desenvolvido como exemplo educacional.

## 👨‍💻 Autor

Desenvolvido seguindo as melhores práticas de desenvolvimento Android com Kotlin.

