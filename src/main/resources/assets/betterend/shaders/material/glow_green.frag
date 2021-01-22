#include frex:shaders/api/fragment.glsl
#include frex:shaders/lib/math.glsl

void frx_startFragment(inout frx_FragmentData fragData) {
	float a = abs(fragData.spriteColor.g - fragData.spriteColor.r);
	float b = abs(fragData.spriteColor.g - fragData.spriteColor.b);
	float l = frx_luminance(fragData.spriteColor.rgb);
	fragData.emissivity = ((l > 0.9) || (a > 0.1 && b > 0.1 && fragData.spriteColor.g > 0.5)) ? 1 : 0;
}
