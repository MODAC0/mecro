import React, { useState, useEffect } from 'react';
import {
  SafeAreaView,
  StyleSheet,
  Text,
  TouchableOpacity,
  View,
  NativeModules,
  DeviceEventEmitter,
  Alert,
} from 'react-native';

const { MecroPermission, MecroImage, FloatingOverlay } = NativeModules;

const App = () => {
  const [isAccessibilityEnabled, setIsAccessibilityEnabled] = useState(false);
  const [isOverlayRunning, setIsOverlayRunning] = useState(false);

  useEffect(() => {
    // 접근성 서비스 상태 변화 감지 콜백
    const subscription = DeviceEventEmitter.addListener(
      'onAccessibilityStatusChanged',
      (event) => {
        setIsAccessibilityEnabled(event.isEnabled);
      }
    );

    // 초기 상태 체크
    checkStatus();

    return () => subscription.remove();
  }, []);

  const checkStatus = async () => {
    try {
      const enabled = await MecroPermission.checkAccessibilityPermission();
      setIsAccessibilityEnabled(enabled);
    } catch(e) {
      console.log("Status check failed", e);
    }
  };

  const startMecro = async () => {
    try {
      // 1. 배터리 최적화 제외 요청
      MecroPermission.requestBatteryOptimizationExemption();
      
      // 2. 미디어 프로젝션(화면 캡처) 요청
      MecroPermission.requestMediaProjection();
      
      Alert.alert("알림", "화면 캡처 권한을 허용하면 매크로 엔진이 시작됩니다.");
    } catch (e) {
      console.error(e);
    }
  };

  const testImageSearchAndClick = async () => {
    // 테스트용 타겟 이미지 경로 (미리 에뮬레이터에 업로드되어 있어야 함)
    const targetPath = '/storage/emulated/0/Download/target.png';
    
    console.log("이미지 검색 시작...");
    try {
      const result = await MecroImage.findTargetImage(targetPath, 0.8);

      if (result.found) {
        console.log(`발견! 좌표: (${result.x}, ${result.y}) - 클릭 실행`);
        await MecroPermission.performClick(result.x, result.y);
      } else {
        Alert.alert("실패", "화면에서 이미지를 찾을 수 없습니다.");
      }
    } catch (e) {
      Alert.alert("에러", String(e));
    }
  };

  const toggleOverlay = () => {
    if (isOverlayRunning) {
      FloatingOverlay.stopOverlay();
    } else {
      FloatingOverlay.startOverlay();
    }
    setIsOverlayRunning(!isOverlayRunning);
  };

  return (
    <SafeAreaView style={styles.container}>
      <View style={styles.header}>
        <Text style={styles.title}>MECRO ENGINE</Text>
        <Text style={styles.status}>
          접근성 권한: {isAccessibilityEnabled ? '✅ 활성' : '❌ 비활성'}
        </Text>
      </View>

      <View style={styles.buttonContainer}>
        <TouchableOpacity style={styles.button} onPress={() => MecroPermission.openAccessibilitySettings()}>
          <Text style={styles.buttonText}>1. 접근성 권한 설정 열기</Text>
        </TouchableOpacity>

        <TouchableOpacity style={[styles.button, { backgroundColor: '#34c759' }]} onPress={startMecro}>
          <Text style={styles.buttonText}>2. 매크로 엔진(캡처) 시작</Text>
        </TouchableOpacity>

        <TouchableOpacity style={[styles.button, { backgroundColor: '#5856d6' }]} onPress={testImageSearchAndClick}>
          <Text style={styles.buttonText}>3. 이미지 찾기 & 클릭 테스트</Text>
        </TouchableOpacity>

        <TouchableOpacity style={[styles.button, { backgroundColor: '#ff9500' }]} onPress={toggleOverlay}>
          <Text style={styles.buttonText}>
            {isOverlayRunning ? '플로팅 오버레이 끄기' : '플로팅 오버레이 켜기'}
          </Text>
        </TouchableOpacity>
      </View>
      
      <View style={styles.footer}>
        <Text style={styles.info}>
          * 테스트 전 /Download/target.png 파일을 에뮬레이터에 넣어주세요.
        </Text>
      </View>
    </SafeAreaView>
  );
};

const styles = StyleSheet.create({
  container: { flex: 1, backgroundColor: '#f2f2f7', padding: 20 },
  header: { marginBottom: 30, alignItems: 'center' },
  title: { fontSize: 28, fontWeight: 'bold', color: '#1c1c1e' },
  status: { fontSize: 16, marginTop: 10, color: '#8e8e93' },
  buttonContainer: { gap: 15 },
  button: {
    backgroundColor: '#007aff',
    padding: 18,
    borderRadius: 14,
    alignItems: 'center',
    shadowColor: '#000',
    shadowOffset: { width: 0, height: 2 },
    shadowOpacity: 0.1,
    shadowRadius: 4,
  },
  buttonText: { color: '#fff', fontSize: 16, fontWeight: '600' },
  footer: { marginTop: 'auto', paddingBottom: 20 },
  info: { fontSize: 12, color: '#8e8e93', textAlign: 'center' }
});

export default App;
