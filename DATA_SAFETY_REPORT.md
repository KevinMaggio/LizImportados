# 📋 Data Safety Report - Liz Importados

## 🎯 **Resumen de Recopilación de Datos**

Esta aplicación **SÍ recopila datos de usuario**, pero de forma **mínima y necesaria** para funcionar.

## 📊 **Datos Recopilados**

### **1. Información de Cuenta (OBLIGATORIO)**
- **Tipo:** Dirección de email
- **Fuente:** Google Sign-In (OAuth)
- **Uso:** 
  - Autenticación del usuario
  - Identificación del carrito de compras
  - Contacto para pedidos via WhatsApp
- **Almacenamiento:** Firebase Authentication
- **¿Se comparte?** NO
- **¿Opcional?** NO (requerido para usar la app)

### **2. Identificadores de Usuario**
- **Tipo:** UID único generado por Firebase
- **Uso:** Relacionar carrito con usuario
- **Almacenamiento:** Firebase Firestore
- **¿Se comparte?** NO
- **¿Opcional?** NO

### **3. Actividad en la App**
- **Tipo:** Productos agregados al carrito
- **Uso:** Funcionalidad de carrito de compras
- **Almacenamiento:** Firebase Firestore
- **¿Se comparte?** NO
- **¿Opcional?** NO (funcionalidad principal)

## 🔒 **Medidas de Seguridad**

### **Cifrado:**
- ✅ Datos en tránsito: HTTPS/TLS
- ✅ Datos en reposo: Cifrado de Firebase
- ✅ Autenticación: OAuth 2.0

### **Acceso:**
- ✅ Solo usuarios autenticados
- ✅ Cada usuario ve solo sus datos
- ✅ No hay acceso de terceros

## 🚫 **Datos QUE NO Recopilamos**

- ❌ Información de ubicación
- ❌ Información financiera (no procesamos pagos)
- ❌ Fotos o archivos
- ❌ Contactos
- ❌ Información del dispositivo
- ❌ Datos de diagnóstico
- ❌ Historial de navegación web

## 📱 **Permisos del Android Manifest**

```xml
<uses-permission android:name="android.permission.INTERNET"/>
```

**Justificación:** Únicamente para comunicación con Firebase y WhatsApp.

## 🎯 **Para Google Play Console**

### **Data Collection:**
- **Collects data:** SÍ
- **Shares data:** NO
- **Encrypted in transit:** SÍ
- **Users can delete data:** SÍ (cerrando cuenta)

### **Types of Data:**
1. **Account info**
   - Email address
   - Required: YES
   - Purpose: App functionality, Account management

2. **App activity**
   - App interactions
   - Required: YES  
   - Purpose: App functionality

3. **App info and performance**
   - None collected

## 🔄 **Retención de Datos**

- Los datos se mantienen mientras la cuenta esté activa
- Usuario puede eliminar cuenta contactando soporte
- Al eliminar cuenta se borran todos los datos asociados

## 📞 **Contacto para Privacidad**

Usuarios pueden contactar para:
- Consultas sobre datos
- Solicitar eliminación de datos
- Reportar problemas de privacidad

**Contacto:** Via WhatsApp integrado en la app

