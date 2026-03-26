import React from 'react';
import {
  View,
  Text,
  StyleSheet,
  TouchableOpacity,
  Dimensions,
  Platform,
} from 'react-native';
import {BlurView} from '@react-native-community/blur';

const {width} = Dimensions.get('window');

const FloatingOverlay = () => {
  return (
    <View style={styles.container}>
      {/* iOS style Glassmorphism Overlay */}
      <BlurView
        style={styles.blurView}
        blurType="light"
        blurAmount={15}
        reducedTransparencyFallbackColor="white">
        <View style={styles.innerContainer}>
          <View style={styles.header}>
            <View style={styles.indicator} />
            <Text style={styles.title}>Mecro Floating</Text>
          </View>
          
          <View style={styles.content}>
            <Text style={styles.description}>
              배경이 반투명하게 보이는{'\n'}플로팅 오버레이입니다.
            </Text>
          </View>

          <TouchableOpacity style={styles.button}>
            <Text style={styles.buttonText}>액션 실행</Text>
          </TouchableOpacity>
        </View>
      </BlurView>
    </View>
  );
};

const styles = StyleSheet.create({
  container: {
    width: width * 0.8,
    borderRadius: 24,
    overflow: 'hidden',
    borderWidth: 1,
    borderColor: 'rgba(255, 255, 255, 0.3)', // iOS style thin border
    backgroundColor: 'rgba(255, 255, 255, 0.4)', // Base transparency
    ...Platform.select({
      ios: {
        shadowColor: '#000',
        shadowOffset: {width: 0, height: 10},
        shadowOpacity: 0.15,
        shadowRadius: 20,
      },
      android: {
        elevation: 8,
      },
    }),
  },
  blurView: {
    padding: 20,
  },
  innerContainer: {
    alignItems: 'center',
  },
  header: {
    width: '100%',
    alignItems: 'center',
    marginBottom: 15,
  },
  indicator: {
    width: 36,
    height: 4,
    backgroundColor: 'rgba(0, 0, 0, 0.1)',
    borderRadius: 2,
    marginBottom: 10,
  },
  title: {
    fontSize: 18,
    fontWeight: '700',
    color: '#1d1d1f', // SF Pro style color
  },
  content: {
    marginBottom: 20,
  },
  description: {
    fontSize: 14,
    color: '#424245',
    textAlign: 'center',
    lineHeight: 20,
  },
  button: {
    width: '100%',
    paddingVertical: 12,
    backgroundColor: '#0071e3', // Apple Blue
    borderRadius: 12,
    alignItems: 'center',
  },
  buttonText: {
    color: 'white',
    fontSize: 16,
    fontWeight: '600',
  },
});

export default FloatingOverlay;
