import { ConfigPlugin, withAndroidManifest } from '@expo/config-plugins';

const withExpoFloatingBubble: ConfigPlugin = (config) => {
  return withAndroidManifest(config, (config) => {
    const manifest = config.modResults;

    // Ensure FOREGROUND_SERVICE and SYSTEM_ALERT_WINDOW permissions
    if (!manifest.manifest['uses-permission']) manifest.manifest['uses-permission'] = [];
    const addPermission = (name: string) => {
      if (!manifest.manifest['uses-permission'].some((p: any) => p['$']['android:name'] === name)) {
        manifest.manifest['uses-permission'].push({ $: { 'android:name': name } });
      }
    };
    addPermission('android.permission.SYSTEM_ALERT_WINDOW');
    addPermission('android.permission.FOREGROUND_SERVICE');
    addPermission('android.permission.FOREGROUND_SERVICE_SPECIAL_USE');

    // Add <service> declaration to <application>
    const app = manifest.manifest.application?.[0];
    if (app) {
      if (!app.service) app.service = [];
      const serviceName = 'expo.modules.floatingbubble.FloatingBubbleService';
      const serviceAlreadyExists = app.service.some((service: any) => {
        return service.$['android:name'] === serviceName;
      });
      if (!serviceAlreadyExists) {
        // Cast to any to add 'property' sub-element
        const service: any = {
          $: {
            'android:name': serviceName,
            'android:exported': 'false',
            'android:foregroundServiceType': 'specialUse',
          },
          property: [
            {
              $: {
                'android:name': 'android.app.PROPERTY_SPECIAL_USE_FGS_SUBTYPE',
                'android:value': 'Floating bubble overlay for always-on top user interaction',
              },
            },
          ],
        };
        app.service.push(service);
      }
    }

    return config;
  });
};

export default withExpoFloatingBubble;
