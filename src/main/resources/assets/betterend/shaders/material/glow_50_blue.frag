#include frex:shaders/api/fragment.glsl
#include frex:shaders/lib/math.glsl

void frx_startFragment(inout frx_FragmentData fragData) {
	float a = abs(fragData.spriteColor.b - fragData.spriteColor.r);
	float b = abs(fragData.spriteColor.b - fragData.spriteColor.g);
	fragData.emissivity = (a > 0.1 && b > 0.1 && fragData.spriteColor.b > 0.5) ? 1 : 0;
}
